package br.gov.caixa.service.simulacao;

import br.gov.caixa.telemetria.MedirTelemetria;
import br.gov.caixa.utils.SimulacaoValidator;
import br.gov.caixa.dto.business.ProdutoDTO;
import br.gov.caixa.dto.simulacao.*;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.entity.simulacao.Simulacao;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.repository.business.ProdutoRepository;
import br.gov.caixa.repository.simulacao.SimulacaoRepository;
import br.gov.caixa.utils.SimulacaoCalculator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulacaoService {

    @Inject
    SimulacaoValidator simulacaoValidator;
    @Inject
    SimulacaoCalculator simulacaoCalculator;
    @Inject
    ProdutoRepository produtoRepository;
    @Inject
    SimulacaoRepository simulacaoRepository;
    @Inject
    ClienteRepository clienteRepository;

    @MedirTelemetria
    public SimulacaoResponse simularInvestimento(SimulacaoRequest request){
        ProdutoInvestimento produtoInvestimento = buscarProdutoCompativel(request.tipoProduto);
        simulacaoValidator.validar(request, produtoInvestimento.getParametroProduto());
        double valorFinal = simulacaoCalculator.calcularValorFinal(request.valor,
                                               produtoInvestimento.getRentabilidade(),
                                               request.prazoMeses);
        double valorArredondado = Math.round(valorFinal * 100.0) / 100.0;
        Simulacao simulacao = registrarSimulacao(request, produtoInvestimento, valorArredondado);
        return montarResposta(produtoInvestimento, valorArredondado, request.prazoMeses,simulacao.getDataSimulacao());
    }

    public List<SimulacaoHistoricoDTO> listarTodas() {
        List<Simulacao> simulacoes = simulacaoRepository.findAllOrdered();
        return simulacoes.stream()
                .map(SimulacaoHistoricoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SimulacaoHistoricoDTO> listarSimulacaoCliente(Long clienteId){
        List<Simulacao> simulacoesCliente = simulacaoRepository.findByCliente(clienteId);
        return simulacoesCliente.stream()
                .map(SimulacaoHistoricoDTO::fromEntity)
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
        orElseThrow(() -> new WebApplicationException("Nenhum produto compatível encontrado: "+
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
