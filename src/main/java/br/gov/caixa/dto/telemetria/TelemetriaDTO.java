package br.gov.caixa.dto.telemetria;

import java.util.List;

public class TelemetriaDTO {

    public List<ServicoTelemetriaDTO> servicos;
    public PeriodoDTO periodo;

    public TelemetriaDTO(List<ServicoTelemetriaDTO> servicos, PeriodoDTO periodo) {
        this.servicos = servicos;
        this.periodo = periodo;
    }
}

