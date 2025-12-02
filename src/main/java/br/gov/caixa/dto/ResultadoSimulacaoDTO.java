package br.gov.caixa.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ResultadoSimulacaoDTO {

    public BigDecimal valorFinalDecimal;
    public Double rentabilidadeEfetiva;
    public Integer prazoMeses;

    public ResultadoSimulacaoDTO(Double valorFinal,
                                 Double rentabilidadeEfetiva,
                                 Integer prazoMeses){
        this.valorFinalDecimal = BigDecimal
                .valueOf(valorFinal)
                .setScale(2, RoundingMode.HALF_UP);
        this.rentabilidadeEfetiva = rentabilidadeEfetiva;
        this.prazoMeses = prazoMeses;
    }
}
