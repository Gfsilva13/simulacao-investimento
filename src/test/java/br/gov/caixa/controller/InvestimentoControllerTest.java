package br.gov.caixa.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.gov.caixa.dto.business.InvestimentoDTO;
import br.gov.caixa.service.business.InvestimentoService;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;

@QuarkusTest
class InvestimentoControllerTest {

    @InjectMock
    InvestimentoService investimentoService;

    @Test
    @TestSecurity(user = "user", roles = {"user"})
    void deveListarInvestimentosDoClienteComSucesso() {
        InvestimentoDTO investimento1 = new InvestimentoDTO();
        investimento1.id = 1L;
        investimento1.valor = new BigDecimal("1000.00");
        investimento1.data = LocalDateTime.now();
        investimento1.rentabilidade = 10.00;

        InvestimentoDTO investimento2 = new InvestimentoDTO();
        investimento2.id = 2L;
        investimento2.valor = new BigDecimal("2000.00");
        investimento2.data = LocalDateTime.now();
        investimento2.rentabilidade = 20.00;

        when(investimentoService.listarPorCliente(1L))
                .thenReturn(List.of(investimento1, investimento2));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/investimento-cliente/1")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(1))
                .body("[1].id", equalTo(2));
    }

    @Test
    @TestSecurity(user = "user", roles = {"user"})
    void deveRetornar404QuandoNaoExistirInvestimentoParaCliente() {

        when(investimentoService.listarPorCliente(1L))
                .thenReturn(List.of());

        given()
                .when()
                .get("/investimento-cliente/1")
                .then()
                .statusCode(404)
                .body(equalTo("Não encontrado nenhum investimento para o cliente ID 1"));
    }

    @Test
    @TestSecurity(user = "user", roles = {"user"})
    void deveRetornar400QuandoClienteIdInvalido() {

        given()
                .when()
                .get("/investimento-cliente/0")
                .then()
                .statusCode(400)
                .body(equalTo("O ID do cliente deve ser um número positivo."));
    }

   @Test
    void deveRetornar401QuandoNaoAutenticado() {

        given()
                .when()
                .get("/investimento-cliente/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void adminTambemPodeListarInvestimentos() {

        when(investimentoService.listarPorCliente(2L))
                .thenReturn(List.of());

        given()
                .when()
                .get("/investimento-cliente/2")
                .then()
                .statusCode(404);
    }
}

