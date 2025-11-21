package br.gov.caixa.service;

import br.gov.caixa.dto.TelemetriaDTO;
import br.gov.caixa.entity.Cliente;
import br.gov.caixa.entity.Investimento;
import br.gov.caixa.entity.Simulacao;
import br.gov.caixa.repository.ClienteRepository;
import br.gov.caixa.repository.InvestimentoRepository;
import br.gov.caixa.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class TelemetriaService {

    @Inject
    SimulacaoRepository simulacaoRepository;

    @Inject
    InvestimentoRepository investimentoRepository;

    @Inject
    ClienteRepository clienteRepository;

    public TelemetriaDTO gerarResumo(int dias){

        LocalDateTime limiteFiltro = LocalDateTime.now().minusDays(dias);

        List<Simulacao> simulacoesRecentes = simulacaoRepository.listAll()
                .stream().filter(s -> s.getDataSimulacao().isAfter(limiteFiltro))
                .toList();

        List<Investimento> investimentosRecentes = investimentoRepository.listAll()
                .stream().filter(i -> i.getData().isAfter(limiteFiltro))
                .toList();

        List<Cliente> clientes = clienteRepository.listAll();

        long totalSimulacoes = simulacoesRecentes.size();
        long totalInvestimentos = investimentosRecentes.size();
        long totalClientes = clientes.size();

        Map<String, Long> simulacoesPorProduto = simulacoesRecentes.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProdutoId().toString(),
                        Collectors.counting()
                ));

        Map<String, Long> perfilPorCliente = clientes.stream()
                .collect(Collectors.groupingBy(
                        Cliente::getPerfilRisco,
                        Collectors.counting()
                ));
        return new TelemetriaDTO(
                totalSimulacoes,
                totalInvestimentos,
                totalClientes,
                simulacoesPorProduto,
                perfilPorCliente
        );
    }
}
