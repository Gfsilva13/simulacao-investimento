package br.gov.caixa.service;

import br.gov.caixa.business.SimulacaoValidator;
import br.gov.caixa.dto.*;
import br.gov.caixa.entity.ParametroProduto;
import br.gov.caixa.entity.ProdutoInvestimento;
import br.gov.caixa.entity.Simulacao;
import br.gov.caixa.repository.ParametroProdutoRepository;
import br.gov.caixa.repository.ProdutoRepository;
import br.gov.caixa.repository.SimulacaoRepository;
import br.gov.caixa.util.SimulacaoUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulacaoService {

    @Inject
    ParametroProdutoRepository parametroProdutoRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    SimulacaoRepository simulacaoRepository;

    public SimulacaoResponse simular(SimulacaoRequest request){
        ParametroProduto parametro = buscarParametroProduto(request.tipoProduto);
                //parametroProdutoRepository.findByTipo(request.tipoProduto);
        SimulacaoValidator.validar(request, parametro);

        ProdutoInvestimento produtoInvestimento = buscarProdutoCompativel(request.tipoProduto, parametro.getRiscoAceito());

        double valorFinal = SimulacaoUtils.calcularValorFinal(request.valor,
                                               produtoInvestimento.getRentabilidade(),
                                               request.prazoMeses);

        Simulacao simulacao = registrarSimulacao(request,produtoInvestimento, valorFinal);

        return montarResposta(produtoInvestimento, valorFinal, request.prazoMeses, simulacao.getDataSimulacao());
    }

    public List<SimulacaoResumoPorProdutoDTO> resumoPorProdutoEDia() {
        List<Simulacao> todas = simulacaoRepository.listAll();

        Map<String, Map<LocalDate, List<Simulacao>>> agrupado = todas.stream()
                .collect(Collectors.groupingBy(
                        s -> {
                            return s.getProduto().getNome();
                        },
                        Collectors.groupingBy(s -> s.getDataSimulacao().toLocalDate())
                ));

        List<SimulacaoResumoPorProdutoDTO> resultado = new ArrayList<>();

        agrupado.forEach((produto, porData) -> {
            porData.forEach((data, simulacoes) -> {
                double media = simulacoes.stream()
                        .mapToDouble(s -> s.getValorFinal())
                        .average()
                        .orElse(0);
                resultado.add(new SimulacaoResumoPorProdutoDTO(produto, data, simulacoes.size(), media));
            });
        });

        return resultado;
    }

    public List<SimulacaoHistoricoDTO> listarTodas() {
        List<Simulacao> simulacoes = simulacaoRepository.findAllOrdered();

        List<ParametroProduto> produtos = parametroProdutoRepository.listAll();

        Map<Long, String> nomesProdutos = produtos.stream()
                .collect(Collectors.toMap(
                        ParametroProduto::getId,
                        ParametroProduto::getTipo
                ));

        return simulacoes.stream()
                .map(s -> SimulacaoHistoricoDTO.fromEntity(s, nomesProdutos.get(s.getProdutoId())))
                .collect(Collectors.toList());
    }

    private ParametroProduto buscarParametroProduto(String tipoProduto) {
        return Optional.ofNullable(parametroProdutoRepository.findByTipo(tipoProduto))
                .orElseThrow(() -> new WebApplicationException("Tipo de produto inválido", 400));
    }

    private ProdutoInvestimento buscarProdutoCompativel(String tipo, String riscoAceito) {
        return produtoRepository.findByTipoAndRisco(tipo, riscoAceito).
        orElseThrow(() -> new WebApplicationException("Nenhum produto compatível encontrado", 404));
    }

    private Simulacao registrarSimulacao(SimulacaoRequest request, ProdutoInvestimento produto, double valorFinal) {
        Simulacao simulacao = new Simulacao();
        simulacao.setClienteId(request.clienteId);
        simulacao.setProdutoId(produto.getId());
        simulacao.setValorInvestido(request.valor);
        simulacao.setValorFinal(valorFinal);
        simulacao.setPrazoMeses(request.prazoMeses);
        simulacao.setDataSimulacao(LocalDateTime.now());
        simulacaoRepository.persist(simulacao);
        return simulacao;
    }

    private SimulacaoResponse montarResposta(ProdutoInvestimento produto, double valorFinal, int prazoMeses, LocalDateTime data) {
        SimulacaoResponse response = new SimulacaoResponse();
        response.produtoValidado = ProdutoDTO.fromEntity(produto);
        response.resultadoSimulacao = new ResultadoSimulacaoDTO(valorFinal, produto.getRentabilidade(), prazoMeses);
        response.dataSimulacao = data;
        return response;
    }
}
