package br.gov.caixa.repository;

import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProdutoRepositoryTest {

    @Inject
    ProdutoRepository repository;

    @Test
    void testFindByTipo() {
        Optional<ProdutoInvestimento> resultadoTipo = repository.findByTipo("CDB");

        assertTrue(resultadoTipo.isPresent());
        assertEquals("CDB", resultadoTipo.get().getTipoProduto());
    }

    @Test
    void testFindByPerfilConservador() {
        List<ProdutoInvestimento> resultadoCon = repository.findByPerfil("Conservador");

        assertEquals(1, resultadoCon.size());
        assertEquals("Baixo", resultadoCon.get(0).getParametroProduto().getRiscoAceito());
    }

    @Test
    void testFindByPerfilModerado() {
        List<ProdutoInvestimento> resultadoMod = repository.findByPerfil("Moderado");

        assertEquals(2, resultadoMod.size());
        List<String> riscos = resultadoMod.stream()
                .map(p -> p.getParametroProduto().getRiscoAceito())
                .toList();
        assertTrue(riscos.contains("Baixo"));
        assertTrue(riscos.contains("Medio"));
    }

    @Test
    void testFindByPerfilAgressivo() {
        List<ProdutoInvestimento> resultadoAgr = repository.findByPerfil("Agressivo");

        assertEquals(2, resultadoAgr.size());
        List<String> riscos = resultadoAgr.stream()
                .map(p -> p.getParametroProduto().getRiscoAceito())
                .toList();
        assertTrue(riscos.contains("Medio"));
        assertTrue(riscos.contains("Alto"));
    }

    @Test
    void testFindByPerfilDesconhecido() {
        List<ProdutoInvestimento> resultadoDesc = repository.findByPerfil("Desconhecido");
        assertTrue(resultadoDesc.isEmpty());
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

