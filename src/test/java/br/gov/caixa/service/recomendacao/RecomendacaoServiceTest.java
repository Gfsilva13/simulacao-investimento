package br.gov.caixa.service.recomendacao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import br.gov.caixa.dto.business.PerfilRiscoDTO;
import br.gov.caixa.dto.business.ProdutoDTO;
import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.ProdutoRepository;
import br.gov.caixa.service.business.PerfilRiscoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;

@QuarkusTest
class RecomendacaoServiceTest {

    @Inject
    RecomendacaoService recomendacaoService;

    @InjectMock
    ProdutoRepository produtoRepository;

    @InjectMock
    PerfilRiscoService perfilRiscoService;

    private ProdutoInvestimento produto(Long id, String nome, String perfil) {
        ParametroProduto parametro = new ParametroProduto();
        parametro.setRiscoAceito(perfil);

        ProdutoInvestimento produtoInvestimento = new ProdutoInvestimento();
        produtoInvestimento.setId(id);
        produtoInvestimento.setNomeProduto(nome);
        produtoInvestimento.setParametroProduto(parametro);
        return produtoInvestimento;
    }

    @Test
    void deveRecomendarProdutosPorPerfil() {
        ProdutoInvestimento p1 = produto(1L, "CDB Conservador", "Conservador");
        ProdutoInvestimento p2 = produto(2L, "LCI Conservador", "Conservador");

        when(produtoRepository.findByPerfil("Conservador"))
                .thenReturn(List.of(p1, p2));

        List<ProdutoDTO> resultado =
                recomendacaoService.recomendarPorPerfil("Conservador");

        assertEquals(2, resultado.size());
        assertEquals("CDB Conservador", resultado.get(0).nome);
        assertEquals("LCI Conservador", resultado.get(1).nome);

        verify(produtoRepository, times(1))
                .findByPerfil("Conservador");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutosParaPerfil() {
        when(produtoRepository.findByPerfil("Moderado"))
                .thenReturn(List.of());

        List<ProdutoDTO> resultado =
                recomendacaoService.recomendarPorPerfil("Moderado");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(produtoRepository, times(1))
                .findByPerfil("Moderado");
    }

    @Test
    void deveRecomendarProdutosPorCliente() {
        PerfilRiscoDTO perfil = new PerfilRiscoDTO();
        perfil.perfil = "Agressivo";

        ProdutoInvestimento p = produto(3L, "ACOES", "Agressivo");

        when(perfilRiscoService.perfilCliente(10L))
                .thenReturn(Optional.of(perfil));

        when(produtoRepository.findByPerfil("Agressivo"))
                .thenReturn(List.of(p));

        List<ProdutoDTO> resultado =
                recomendacaoService.recomendarPorCliente(10L);

        assertEquals(1, resultado.size());
        assertEquals("ACOES", resultado.get(0).nome);

        verify(perfilRiscoService, times(1))
                .perfilCliente(10L);

        verify(produtoRepository, times(1))
                .findByPerfil("Agressivo");
    }

    @Test
    void deveLancarErro404QuandoPerfilDoClienteNaoEncontrado() {
        when(perfilRiscoService.perfilCliente(99L))
                .thenReturn(Optional.empty());

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> recomendacaoService.recomendarPorCliente(99L)
        );

        assertEquals(404, ex.getResponse().getStatus());
        assertEquals("Perfil não encontrado", ex.getMessage());

        verify(perfilRiscoService, times(1))
                .perfilCliente(99L);

        verifyNoInteractions(produtoRepository);
    }
}

