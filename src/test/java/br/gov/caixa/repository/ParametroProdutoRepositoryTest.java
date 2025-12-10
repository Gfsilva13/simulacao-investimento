package br.gov.caixa.repository;

import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.repository.business.ParametroProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ParametroProdutoRepositoryTest {

    @Inject
    ParametroProdutoRepository repository;

    @BeforeEach
    @Transactional
    void setUp(){
        repository.deleteAll();

        ParametroProduto parametro = new ParametroProduto();
        parametro.setMaxPrazo(36);
        parametro.setMinValor(10.00);
        parametro.setRiscoAceito("Baixo");
        repository.persist(parametro);
    }

    @Test
    void testFindByTipo_RetornaParametroExistente() {
        ParametroProduto resultado = repository.findByRisco("Baixo");
        assertNotNull(resultado);
        assertEquals("Baixo", resultado.getRiscoAceito());
    }

    @Test
    void testFindByRisco_RetornaNullQuandoNaoExiste() {
        ParametroProduto resultado = repository.findByRisco("Inexistente");
        assertNull(resultado);
    }
}

