package br.gov.caixa.dto;

public class ResultadoSimulacaoDTO {

    public Double valorFinal;
    public Double rentabilidadeEfetiva;
    public Integer prazoMeses;

    public ResultadoSimulacaoDTO(Double valorFinal,
                                 Double rentabilidadeEfetiva,
                                 Integer prazoMeses){
        this.valorFinal = valorFinal;
        this.rentabilidadeEfetiva = rentabilidadeEfetiva;
        this.prazoMeses = prazoMeses;
    }
}
