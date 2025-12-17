package br.gov.caixa.exception;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ValidationExceptionMapperTest {

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void testValidationExceptionIsMapped() {
        RestAssured.given()
                .when()
                .get("/test-validation?nome= ")
                .then()
                .statusCode(400)
                .body(equalTo("Erro de validaçao: Nome é obrigatório"));
    }
}

