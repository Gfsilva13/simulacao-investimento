package br.gov.caixa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.gov.caixa.dto.business.InvestimentoDTO;
import br.gov.caixa.entity.business.Investimento;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.InvestimentoRepository;
import br.gov.caixa.service.business.InvestimentoService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@QuarkusTest
public class InvestimentoServiceTest {

    @Inject
    InvestimentoService investimentoService;

    @InjectMock
    InvestimentoRepository investimentoRepository;

    private Investimento criarInvestimento(double valor, int prazoMeses) {
        Investimento investimento = new Investimento();
        ProdutoInvestimento produtoInvestimento= new ProdutoInvestimento();
        produtoInvestimento.setTipoProduto("CDB");
        investimento.setValor(valor);
        investimento.setRentabilidade(0.12);
        investimento.setProdutoInvestimento(produtoInvestimento);
        investimento.setPrazoMeses(prazoMeses);
        investimento.setData(LocalDateTime.now().minusMonths(prazoMeses));
        return investimento;

    }

    @Test
    public void deveRetornarListaDeInvestimentosDTOParaCliente() {
        Long clienteId = 1L;
        List<Investimento> investimentos = List.of(
                criarInvestimento(1000, 6),
                criarInvestimento(2000, 12)
        );
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(investimentos);
        List<InvestimentoDTO> resultado = investimentoService.listarPorCliente(clienteId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1000, resultado.get(0).valor);
        assertEquals(2000, resultado.get(1).valor);

        verify(investimentoRepository).findByCliente(clienteId);
    }

    @Test
    public void deveRetornarListaVaziaQuandoClienteNaoTemInvestimentos() {
        Long clienteId = 2L;
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(List.of());

        List<InvestimentoDTO> resultado = investimentoService.listarPorCliente(clienteId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(investimentoRepository).findByCliente(clienteId);
    }
}

