package br.gov.caixa.dto;

import java.util.Map;

public class TelemetriaDTO {
    public long totalSimulacoes;
    public long totalInvestimentos;
    public long totalClientes;
    public Map<String, Long> simulacoesPorProduto;
    public Map<String, Long> perfilPorCliente;

    public TelemetriaDTO(long totalSimulacoes, long totalInvestimentos, long totalClientes,
                         Map<String, Long> simulacoesPorProduto,
                         Map<String, Long> perfilPorCliente) {
        this.totalSimulacoes = totalSimulacoes;
        this.totalInvestimentos = totalInvestimentos;
        this.totalClientes = totalClientes;
        this.simulacoesPorProduto = simulacoesPorProduto;
        this.perfilPorCliente = perfilPorCliente;
    }
}
