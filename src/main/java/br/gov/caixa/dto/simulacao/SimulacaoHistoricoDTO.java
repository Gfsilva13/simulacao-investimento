package br.gov.caixa.dto.simulacao;

import br.gov.caixa.entity.simulacao.Simulacao;

import java.time.LocalDateTime;

public class SimulacaoHistoricoDTO {
    public Long id;
    public Long clienteId;
    public String produto;
    public Double valorInvestido;
    public Double valorFinal;
    public Integer prazoMeses;
    public LocalDateTime dataSimulacao;

    public static SimulacaoHistoricoDTO fromEntity(Simulacao simulacao) {
        SimulacaoHistoricoDTO dto = new SimulacaoHistoricoDTO();
        dto.id = simulacao.getId();
        dto.clienteId = simulacao.getCliente().getId();
        dto.produto = simulacao.getProduto().getNomeProduto();
        dto.valorInvestido = simulacao.getValorInvestido();
        dto.valorFinal = simulacao.getValorFinal();
        dto.prazoMeses = simulacao.getPrazoMeses();
        dto.dataSimulacao = simulacao.getDataSimulacao();
        return dto;
    }
}
