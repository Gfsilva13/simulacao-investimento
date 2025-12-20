package br.gov.caixa.dto.simulacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimulacaoResumoPorProdutoDTO {
    public String produto;
    public LocalDate data;
    public int quantidadeSimulacoes;
    public BigDecimal mediaValorFinal;

    public SimulacaoResumoPorProdutoDTO(String produto, LocalDate data, int qtd, BigDecimal media) {
        this.produto = produto;
        this.data = data;
        this.quantidadeSimulacoes = qtd;
        this.mediaValorFinal = media;
    }
}
