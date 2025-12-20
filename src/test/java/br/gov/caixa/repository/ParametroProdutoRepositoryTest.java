package br.gov.caixa.repository;

import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.repository.business.ParametroProdutoRepository;
import br.gov.caixa.repository.business.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ParametroProdutoRepositoryTest {

    @Inject
    ParametroProdutoRepository repository;

    @Inject
    ProdutoRepository produto;

    @Test
    void testFindByRisco_RetornaNullQuandoNaoExiste() {
        ParametroProduto resultadoInex = repository.findByRisco("Inexistente");
        assertNull(resultadoInex);
    }

    @Test
    void deveEncontrarParametroPorRisco() {
        ParametroProduto resultado = repository.findByRisco("Baixo");

        assertNotNull(resultado);
        assertEquals("Baixo", resultado.getRiscoAceito());
        assertTrue(resultado.getMinValor().compareTo(new BigDecimal("100.00")) == 0);
        assertEquals(36, resultado.getMaxPrazo());
    }
}

