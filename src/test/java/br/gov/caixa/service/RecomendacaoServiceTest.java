package br.gov.caixa.service;

import br.gov.caixa.dto.PerfilRiscoDTO;
import br.gov.caixa.dto.ProdutoDTO;
import br.gov.caixa.entity.ProdutoInvestimento;
import br.gov.caixa.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecomendacaoServiceTest {

    @InjectMocks
    RecomendacaoService recomendacaoService;

    @Mock
    PerfilRiscoService perfilRiscoService;

    @Mock
    ProdutoRepository produtoRepository;

    @Test
    void deveRecomendarProdutosParaPerfilModerado() {
        String perfil = "Moderado";

        ProdutoInvestimento produto1 = new ProdutoInvestimento();
        produto1.setId(1L);
        produto1.setNome("CDB Moderado");
        produto1.setTipoProduto("CDB");
        produto1.setRisco("Moderado");

        ProdutoInvestimento produto2 = new ProdutoInvestimento();
        produto2.setId(2L);
        produto2.setNome("LCI Moderado");
        produto2.setTipoProduto("LCI");
        produto2.setRisco("Moderado");

        when(produtoRepository.findByPerfil(perfil)).thenReturn(List.of(produto1, produto2));

        List<ProdutoDTO> recomendados = recomendacaoService.recomendarPorPerfil(perfil);

        assertEquals(2, recomendados.size());
        assertTrue(recomendados.stream().anyMatch(p -> p.nome.equals("CDB Moderado")));
        assertTrue(recomendados.stream().anyMatch(p -> p.nome.equals("LCI Moderado")));
    }

    @Test
    void deveRecomendarProdutosParaClienteComPerfilModerado() {
        Long clienteId = 10L;

        PerfilRiscoDTO perfil = new PerfilRiscoDTO();
        perfil.clienteId = clienteId;
        perfil.perfil = "Moderado";
        perfil.pontuacao = 55;
        perfil.descricao = "Perfil equilibrado";

        ProdutoInvestimento produto1 = new ProdutoInvestimento();
        produto1.setId(1L);
        produto1.setNome("CDB Moderado");
        produto1.setTipoProduto("CDB");
        produto1.setRisco("Moderado");

        ProdutoInvestimento produto2 = new ProdutoInvestimento();
        produto2.setId(2L);
        produto2.setNome("LCI Moderado");
        produto2.setTipoProduto("LCI");
        produto2.setRisco("Moderado");

        when(perfilRiscoService.calcularPerfil(clienteId)).thenReturn(Optional.of(perfil));
        when(produtoRepository.findByPerfil("Moderado")).thenReturn(List.of(produto1, produto2));

        List<ProdutoDTO> recomendados = recomendacaoService.recomendarPorCliente(clienteId);

        assertEquals(2, recomendados.size());
        assertTrue(recomendados.stream().anyMatch(p -> p.nome.equals("CDB Moderado")));
        assertTrue(recomendados.stream().anyMatch(p -> p.nome.equals("LCI Moderado")));
    }


}
