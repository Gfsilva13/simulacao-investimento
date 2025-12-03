package br.gov.caixa.dto.simulacao;

import java.time.LocalDate;

public class SimulacaoResumoPorProdutoDTO {
    public String produto;
    public LocalDate data;
    public int quantidadeSimulacoes;
    public double mediaValorFinal;

    public SimulacaoResumoPorProdutoDTO(String produto, LocalDate data, int qtd, double media) {
        this.produto = produto;
        this.data = data;
        this.quantidadeSimulacoes = qtd;
        this.mediaValorFinal = media;
    }
}
