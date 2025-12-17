package br.gov.caixa.repository;

import br.gov.caixa.entity.telemetria.Telemetria;
import br.gov.caixa.repository.telemetria.TelemetriaRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TelemetriaRepositoryTest {

    @Inject
    TelemetriaRepository telemetriaRepository;

    @Test
    @Transactional
    void deveRetornarTelemetriasDentroDoPeriodo() {
        Telemetria telemetria1 = new Telemetria();
        telemetria1.periodoInicial = LocalDate.of(2025, 11, 1);
        telemetria1.periodoFim = LocalDate.of(2025, 11, 10);
        telemetriaRepository.persist(telemetria1);

        Telemetria telemetria2 = new Telemetria();
        telemetria2.periodoInicial = LocalDate.of(2025, 12, 1);
        telemetria2.periodoFim = LocalDate.of(2025, 12, 10);
        telemetriaRepository.persist(telemetria2);

        Telemetria foraDoPeriodo = new Telemetria();
        foraDoPeriodo.periodoInicial = LocalDate.of(2024, 12, 1);
        foraDoPeriodo.periodoFim = LocalDate.of(2025, 12, 31);
        telemetriaRepository.persist(foraDoPeriodo);

        LocalDate inicio = LocalDate.of(2025, 11, 1);
        LocalDate fim = LocalDate.of(2025, 12, 15);

        List<Telemetria> resultado =
                telemetriaRepository.findByPeriodo(inicio, fim);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(telemetria1));
        assertTrue(resultado.contains(telemetria2));
        assertFalse(resultado.contains(foraDoPeriodo));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverRegistros() {
        List<Telemetria> resultadoTelemetria =
                telemetriaRepository.findByPeriodo(
                        LocalDate.of(2030, 1, 1),
                        LocalDate.of(2030, 12, 31)
                );

        assertTrue(resultadoTelemetria.isEmpty());
    }
}

