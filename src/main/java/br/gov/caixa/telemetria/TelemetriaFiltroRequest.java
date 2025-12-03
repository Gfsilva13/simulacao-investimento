package br.gov.caixa.telemetria;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.QueryParam;

public class TelemetriaFiltroRequest {

    @QueryParam("dias")
    @Min(value = 1, message = "O número mínimo de dias é 1")
    @Max(value = 180, message = "O número máximo de dias é 180")
    public int dias;

}
