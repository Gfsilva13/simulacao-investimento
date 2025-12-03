package br.gov.caixa.service;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@Testcontainers
@QuarkusTestResource(SqlServerTestResource.class)
public class SimulacaoControllerIT {

    @Test
    void deveSimularInvestimentoComSucesso() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId = 101L;
        request.valor = 10000.0;
        request.prazoMeses = 12;
        request.tipoProduto = "CDB";

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes/simular-investimento")
                .then()
                .statusCode(201)
                .body("produtoValidado.nome", equalTo("CDB Real"))
                .body("resultadoSimulacao.valorFinal", closeTo(11200.0, 0.01))
                .body("dataSimulacao", notNullValue());
    }
}
