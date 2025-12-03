package br.gov.caixa.dto.telemetria;


public class ServicoTelemetriaDTO {

    public String nome;
    public long quantidadeChamadas;
    public long mediaTempoRespostaMs;

    public ServicoTelemetriaDTO(String nome, long quantidadeChamadas, long mediaTempoRespostaMs) {
        this.nome = nome;
        this.quantidadeChamadas = quantidadeChamadas;
        this.mediaTempoRespostaMs = mediaTempoRespostaMs;
    }
}
