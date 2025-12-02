package br.gov.caixa.dto;

import br.gov.caixa.entity.Investimento;

import java.time.LocalDateTime;

public class InvestimentoDTO {

    public Long id;
    public String tipo;
    public Double valor;
    public Double rentabilidade;
    public LocalDateTime data;

    public static InvestimentoDTO fromEntity(Investimento investimento) {
        InvestimentoDTO dto = new InvestimentoDTO();
        dto.id = investimento.getId();
        dto.tipo = investimento.getProdutoInvestimento().getTipoProduto();
        dto.valor = investimento.getValor();
        dto.rentabilidade = investimento.getRentabilidade();
        dto.data = investimento.getData();
        return dto;
    }
}
