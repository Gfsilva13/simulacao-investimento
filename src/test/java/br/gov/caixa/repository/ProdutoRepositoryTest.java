package br.gov.caixa.repository;

import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.ProdutoRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProdutoRepositoryTest {

    @Inject
    ProdutoRepository repository;

    private ProdutoInvestimento produto1, produto2, produto3;
    private ParametroProduto parametro1, parametro2, parametro3;

    @BeforeEach
    @Transactional
    void setUp() {
        repository.deleteAll();

        parametro1 = new ParametroProduto();
        parametro1.setRiscoAceito("Baixo");
        parametro1.setMaxPrazo(36);
        parametro1.setMinValor(10.0);
        repository.getEntityManager().persist(parametro1);

        produto1 = new ProdutoInvestimento();
        produto1.setNomeProduto("1");
        produto1.setTipoProduto("CDB");
        produto1.setParametroProduto(parametro1);
        produto1.setRentabilidade(0.5);
        repository.persist(produto1);

        parametro2 = new ParametroProduto();
        parametro2.setRiscoAceito("Medio");
        parametro2.setMaxPrazo(36);
        parametro2.setMinValor(10.0);
        repository.getEntityManager().persist(parametro2);

        produto2 = new ProdutoInvestimento();
        produto2.setNomeProduto("2");
        produto2.setTipoProduto("RF");
        produto2.setParametroProduto(parametro2);
        produto2.setRentabilidade(1.0);
        repository.persist(produto2);

        parametro3 = new ParametroProduto();
        parametro3.setRiscoAceito("Alto");
        parametro3.setMaxPrazo(36);
        parametro3.setMinValor(10.0);
        repository.getEntityManager().persist(parametro3);

        produto3 = new ProdutoInvestimento();
        produto3.setNomeProduto("3");
        produto3.setTipoProduto("LCI");
        produto3.setParametroProduto(parametro3);
        produto3.setRentabilidade(1.5);
        repository.persist(produto3);
    }

    @Test
    void testFindByTipo() {
        Optional<ProdutoInvestimento> resultado = repository.findByTipo("CDB");

        assertTrue(resultado.isPresent());
        assertEquals("CDB", resultado.get().getTipoProduto());
    }

    @Test
    void testFindByPerfilConservador() {
        List<ProdutoInvestimento> resultado = repository.findByPerfil("Conservador");

        assertEquals(1, resultado.size());
        assertEquals("Baixo", resultado.get(0).getParametroProduto().getRiscoAceito());
    }

    @Test
    void testFindByPerfilModerado() {
        List<ProdutoInvestimento> resultado = repository.findByPerfil("Moderado");

        assertEquals(2, resultado.size());
        List<String> riscos = resultado.stream()
                .map(p -> p.getParametroProduto().getRiscoAceito())
                .toList();
        assertTrue(riscos.contains("Baixo"));
        assertTrue(riscos.contains("Medio"));
    }

    @Test
    void testFindByPerfilAgressivo() {
        List<ProdutoInvestimento> resultado = repository.findByPerfil("Agressivo");

        assertEquals(2, resultado.size());
        List<String> riscos = resultado.stream()
                .map(p -> p.getParametroProduto().getRiscoAceito())
                .toList();
        assertTrue(riscos.contains("Medio"));
        assertTrue(riscos.contains("Alto"));
    }

    @Test
    void testFindByPerfilDesconhecido() {
        List<ProdutoInvestimento> resultado = repository.findByPerfil("Desconhecido");
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testListAllOrderedByRentabilidadeDesc() {
        List<ProdutoInvestimento> lista = repository.listAllOrderedByRentabilidadeDesc();
        assertEquals(3, lista.size());
        assertEquals("LCI", lista.get(0).getTipoProduto());
        assertEquals("CDB", lista.get(2).getTipoProduto());
    }

    @Test
    void testFindByRisco() {
        List<ProdutoInvestimento> baixo = repository.findByRisco("BAIXO");
        assertEquals(1, baixo.size());

        List<ProdutoInvestimento> medio = repository.findByRisco("MEDIO");
        assertEquals(1, medio.size());

        List<ProdutoInvestimento> alto = repository.findByRisco("ALTO");
        assertEquals(1, alto.size());

        List<ProdutoInvestimento> inexistente = repository.findByRisco("MUITO ALTO");
        assertTrue(inexistente.isEmpty());
    }
}

