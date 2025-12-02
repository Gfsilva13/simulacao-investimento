package br.gov.caixa.service;

import br.gov.caixa.business.SimulacaoValidator;
import br.gov.caixa.dto.*;
import br.gov.caixa.entity.ParametroProduto;
import br.gov.caixa.entity.ProdutoInvestimento;
import br.gov.caixa.entity.Simulacao;
import br.gov.caixa.repository.ClienteRepository;
import br.gov.caixa.repository.ParametroProdutoRepository;
import br.gov.caixa.repository.ProdutoRepository;
import br.gov.caixa.repository.SimulacaoRepository;
import br.gov.caixa.util.SimulacaoUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    SimulacaoValidator simulacaoValidator;
    @Inject
    SimulacaoUtils simulacaoUtils;
    @Inject
    ProdutoRepository produtoRepository;
    @Inject
    SimulacaoRepository simulacaoRepository;
    @Inject
    ClienteRepository clienteRepository;

    public SimulacaoResponse simular(SimulacaoRequest request){
        ProdutoInvestimento produtoInvestimento = buscarProdutoCompativel(request.tipoProduto);
        simulacaoValidator.validar(request, produtoInvestimento.getParametroProduto());
        double valorFinal = simulacaoUtils.calcularValorFinal(request.valor,
                                               produtoInvestimento.getRentabilidade(),
                                               request.prazoMeses);
        Simulacao simulacao = registrarSimulacao(request, produtoInvestimento, valorFinal);
        return montarResposta(produtoInvestimento, valorFinal, request.prazoMeses,simulacao.getDataSimulacao());
    }

    public List<SimulacaoHistoricoDTO> listarTodas() {
        List<Simulacao> simulacoes = simulacaoRepository.findAllOrdered();
        Map<Long, String> nomesProdutos = simulacoes.stream()
                .collect(Collectors.toMap(
                        Simulacao::getId,
                        s->s.getProduto().getNomeProduto()
                ));
        return simulacoes.stream()
                .map(s -> SimulacaoHistoricoDTO.fromEntity(s))
                .collect(Collectors.toList());
    }

    public List<SimulacaoResumoPorProdutoDTO> resumoPorProdutoEDia() {
        List<Simulacao> todas = simulacaoRepository.listAll();
        Map<String, Map<LocalDate, List<Simulacao>>> agrupado = todas.stream()
                .collect(Collectors.groupingBy(
                        s -> {
                            return s.getProduto().getNomeProduto();
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

    private ProdutoInvestimento buscarProdutoCompativel(String tipo) {
        return produtoRepository.findByTipo(tipo).
        orElseThrow(() -> new WebApplicationException("Nenhum produto compatível encontrado: TIPO: "+
                tipo, 404));
    }

    private SimulacaoResponse montarResposta(ProdutoInvestimento produto, double valorFinal, int prazoMeses, LocalDateTime data) {
        SimulacaoResponse response = new SimulacaoResponse();
        response.produtoValidado = ProdutoDTO.fromEntity(produto);
        response.resultadoSimulacao = new ResultadoSimulacaoDTO(valorFinal, produto.getRentabilidade(), prazoMeses);
        response.dataSimulacao = data;
        return response;
    }

    @Transactional
    public Simulacao registrarSimulacao(SimulacaoRequest request, ProdutoInvestimento produto, double valorFinal) {
        Simulacao simulacao = new Simulacao();
        simulacao.setCliente(clienteRepository.findById(request.clienteId));
        simulacao.setProduto(produtoRepository.findById(produto.getId()));
        simulacao.setValorInvestido(request.valor);
        simulacao.setValorFinal(valorFinal);
        simulacao.setPrazoMeses(request.prazoMeses);
        simulacao.setDataSimulacao(LocalDateTime.now());
        simulacaoRepository.persist(simulacao);
        return simulacao;
    }
}
