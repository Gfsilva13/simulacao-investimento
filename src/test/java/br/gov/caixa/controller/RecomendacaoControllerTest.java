package br.gov.caixa.controller;

import br.gov.caixa.service.recomendacao.RecomendacaoService;
import br.gov.caixa.dto.business.ProdutoDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
class RecomendacaoControllerTest {

    @InjectMock
    RecomendacaoService recomendacaoService;

    @Test
    @TestSecurity(user = "user", roles = {"user"})
    void deveRetornarRecomendacoesPorCliente() {
        ProdutoDTO produto1 = new ProdutoDTO();
        produto1.nome = "CDB";
        ProdutoDTO produto2 = new ProdutoDTO();
        produto2.nome = "RF";

        List<ProdutoDTO> produtos = List.of(produto1, produto2);

        when(recomendacaoService.recomendarPorCliente(1L))
                .thenReturn(produtos);

        given()
                .when()
                .get("/recomendacoes/cliente/1")
                .then()
                .statusCode(200);
        verify(recomendacaoService).recomendarPorCliente(1L);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void deveRetornarRecomendacoesPorPerfil() {
        ProdutoDTO produto3 = new ProdutoDTO();
        produto3.nome = "ACOES";
        ProdutoDTO produto4 = new ProdutoDTO();
        produto4.nome = "LCI";

        List<ProdutoDTO> produtos = List.of(produto3, produto4);

        when(recomendacaoService.recomendarPorPerfil("Agressivo"))
                .thenReturn(produtos);

        given()
                .when()
                .get("/recomendacoes/perfil/Agressivo")
                .then()
                .statusCode(200);

        verify(recomendacaoService).recomendarPorPerfil("Agressivo");
    }

    @Test
    @TestSecurity(user = "guest", roles = {"guest"})
    void deveRetornar403QuandoUsuarioSemPermissao() {

        given()
                .when()
                .get("/recomendacoes/cliente/1")
                .then()
                .statusCode(403);

        verifyNoInteractions(recomendacaoService);
    }

    @Test
    void deveRetornar401QuandoNaoAutenticado() {

        given()
                .when()
                .get("/recomendacoes/cliente/1")
                .then()
                .statusCode(401);

        verifyNoInteractions(recomendacaoService);
    }
}
