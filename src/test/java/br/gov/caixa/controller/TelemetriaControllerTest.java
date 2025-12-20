package br.gov.caixa.controller;

import br.gov.caixa.dto.telemetria.PeriodoDTO;
import br.gov.caixa.dto.telemetria.ServicoTelemetriaDTO;
import br.gov.caixa.dto.telemetria.TelemetriaDTO;
import br.gov.caixa.service.telemetria.TelemetriaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.equalTo;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
class TelemetriaControllerTest {

    @InjectMock
    TelemetriaService telemetriaService;

    @Test
    @TestSecurity(user = "adminUser", roles = {"admin"})
    void deveRetornarResumoQuandoUsuarioAdmin() {
        ServicoTelemetriaDTO servicoTelemetriaDTO = new ServicoTelemetriaDTO("simulacao", 100, 100);
        PeriodoDTO periodoDTO = new PeriodoDTO("inicio", "fim");
        List<ServicoTelemetriaDTO> servicos = new ArrayList<>();
        servicos.add(servicoTelemetriaDTO);
        TelemetriaDTO dto = new TelemetriaDTO(servicos, periodoDTO);

        when(telemetriaService.gerarResumo(2))
                .thenReturn(dto);

        given()
                .queryParam("dias", 2)
                .when()
                .get("/telemetria")
                .then()
                .statusCode(200);
        verify(telemetriaService).gerarResumo(2);
    }

    @Test
    @TestSecurity(user = "user", roles = {"user"})
    void deveRetornar403QuandoUsuarioNaoAdmin() {

        given()
                .when()
                .get("/telemetria")
                .then()
                .statusCode(403);

        verifyNoInteractions(telemetriaService);
    }

    @Test
    void deveRetornar401QuandoNaoAutenticado() {

        given()
                .when()
                .get("/telemetria")
                .then()
                .statusCode(401);

        verifyNoInteractions(telemetriaService);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void deveRetornar400QuandoDiasInvalido() {

        given()
                .queryParam("dias", 0)
                .when()
                .get("/telemetria")
                .then()
                .statusCode(400);

        verifyNoInteractions(telemetriaService);
    }
}
