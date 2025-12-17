package br.gov.caixa.service.telemetria;

import br.gov.caixa.dto.telemetria.ServicoTelemetriaDTO;
import br.gov.caixa.dto.telemetria.TelemetriaDTO;
import br.gov.caixa.entity.telemetria.Telemetria;
import br.gov.caixa.repository.telemetria.TelemetriaRepository;
import br.gov.caixa.telemetria.TelemetriaMetrics;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class TelemetriaServiceTest {

    @InjectMock
    TelemetriaMetrics telemetriaMetrics;

    @InjectMock
    TelemetriaRepository telemetriaRepository;

    @Inject
    TelemetriaService telemetriaService;

    @BeforeEach
    void setup() {
        when(telemetriaMetrics.getChamadas("perfilRisco")).thenReturn(10.0);
        when(telemetriaMetrics.getTempoMedio("perfilRisco")).thenReturn(50.0);

        when(telemetriaMetrics.getChamadas("simularInvestimento")).thenReturn(5.0);
        when(telemetriaMetrics.getTempoMedio("simularInvestimento")).thenReturn(100.0);
    }

    @Test
    void testGerarResumo() {
        int dias = 7;

        TelemetriaDTO resultado = telemetriaService.gerarResumo(dias);

        ZoneId zone = ZoneId.of("America/Sao_Paulo");
        LocalDate fimEsperado = LocalDate.now(zone);
        LocalDate inicioEsperado = fimEsperado.minusDays(dias);

        assertEquals(inicioEsperado.toString(), resultado.periodo.inicio);
        assertEquals(fimEsperado.toString(), resultado.periodo.fim);

        List<ServicoTelemetriaDTO> servicos = resultado.servicos;
        assertEquals(2, servicos.size());

        ServicoTelemetriaDTO perfil = servicos.stream()
                .filter(s -> s.nome.equals("perfil-risco"))
                .findFirst()
                .orElseThrow();

        assertEquals(10L, perfil.quantidadeChamadas);
        assertEquals(50L, perfil.mediaTempoRespostaMs);

        ServicoTelemetriaDTO simular = servicos.stream()
                .filter(s -> s.nome.equals("simular-investimento"))
                .findFirst()
                .orElseThrow();

        assertEquals(5L, simular.quantidadeChamadas);
        assertEquals(100L, simular.mediaTempoRespostaMs);

        verify(telemetriaRepository, times(2)).persist(any(Telemetria.class));
    }
}
