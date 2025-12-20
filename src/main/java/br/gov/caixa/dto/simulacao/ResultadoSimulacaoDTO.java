package br.gov.caixa.dto.simulacao;

import java.math.BigDecimal;

public class ResultadoSimulacaoDTO {

    public BigDecimal valorFinal;
    public Double rentabilidadeEfetiva;
    public Integer prazoMeses;

    public ResultadoSimulacaoDTO(BigDecimal valorFinal,
                                 Double rentabilidadeEfetiva,
                                 Integer prazoMeses){
        this.valorFinal = valorFinal;
        this.rentabilidadeEfetiva = rentabilidadeEfetiva;
        this.prazoMeses = prazoMeses;
    }
}
