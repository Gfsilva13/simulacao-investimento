package br.gov.caixa.service.simulacao;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.service.SqlServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@Testcontainers
@QuarkusTestResource(SqlServerTestResource.class)
public class SimulacaoControllerTest {

    @Inject
    ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    @Transactional
    void setup() {
        cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setPerfilRisco("Conservador");
        cliente.setPontuacao(10);
        clienteRepository.persist(cliente);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void deveSimularInvestimentoComSucesso() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId = cliente.getId();
        request.valor = 10000.0;
        request.prazoMeses = 12;
        request.tipoProduto = "CDB";

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simular-investimento/")
                .then()
                .statusCode(201)
                .body("produtoValidado.tipo", equalTo("CDB"))
                .body("dataSimulacao", notNullValue());

        Float valorFinal= (given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simular-investimento/")
                .then()
                .statusCode(201)
                .extract()
                .path("resultadoSimulacao.valorFinal"));
        Double valorTipoDouble = valorFinal.doubleValue();

        assertThat(valorTipoDouble, closeTo(11200.0, 0.01));
    }
}
