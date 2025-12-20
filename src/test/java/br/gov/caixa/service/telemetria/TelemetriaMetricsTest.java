package br.gov.caixa.service.telemetria;

import br.gov.caixa.telemetria.TelemetriaMetrics;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

@QuarkusTest
class TelemetriaMetricsTest {

    @Inject
    TelemetriaMetrics telemetriaMetrics;

    @Test
    void deveRetornarTempoMedioQuandoServicoExiste() {
        telemetriaMetrics.registrarChamada("simulacao1", 10);
        double resultado = telemetriaMetrics.getTempoMedio("simulacao1");

        assertEquals(10, resultado, 0.0);
    }

    @Test
    void deveRetornarZeroQuandoServicoNaoExisteTempo() {
        double resultado = telemetriaMetrics.getTempoMedio("inexistente");

        assertEquals(0.0, resultado, 0.0);
    }

    @Test
    void deveRetornarQuantidadeChamadasQuandoServicoExiste() {
        telemetriaMetrics.registrarChamada("simulacao2", 7);
        telemetriaMetrics.registrarChamada("simulacao2", 5);
        double resultado = telemetriaMetrics.getChamadas("simulacao2");

        assertEquals(2, resultado, 0.0);
    }

    @Test
    void deveRetornarZeroQuandoServicoNaoExisteChamadas() {
        double resultado = telemetriaMetrics.getChamadas("inexistente");

        assertEquals(0.0, resultado, 0.0);
    }
}
