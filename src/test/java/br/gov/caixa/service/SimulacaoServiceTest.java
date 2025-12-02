package br.gov.caixa.service;

import br.gov.caixa.dto.SimulacaoRequest;
import br.gov.caixa.dto.SimulacaoResponse;
import br.gov.caixa.entity.ParametroProduto;
import br.gov.caixa.entity.ProdutoInvestimento;
import br.gov.caixa.repository.ParametroProdutoRepository;
import br.gov.caixa.repository.ProdutoRepository;
import br.gov.caixa.repository.SimulacaoRepository;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimulacaoServiceTest {

    @InjectMocks
    SimulacaoService simulacaoService;

    @Mock
    ParametroProdutoRepository parametroProdutoRepository;

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    SimulacaoRepository simulacaoRepository;

    @Test
    void deveSimularInvestimentoComProdutoValido() {
        ParametroProduto parametro = new ParametroProduto();
        //parametro.setTipo("CDB");
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");

        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setId(101L);
        produto.setNomeProduto("CDB Caixa 2026");
        produto.setTipoProduto("CDB");
        produto.setRentabilidade(0.12);
        //produto.setRisco("Baixo");

        when(parametroProdutoRepository.findByTipo("CDB")).thenReturn(parametro);
//        when(produtoRepository.findByTipoAndRisco("CDB", "Baixo"))
//                .thenReturn(Optional.of(produto));

        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId = 123L;
        request.valor = 10000.0;
        request.prazoMeses = 12;
        request.tipoProduto = "CDB";

        SimulacaoResponse response = simulacaoService.simular(request);

        //assertEquals(11200.0, response.resultadoSimulacao.valorFinal, 0.01);
        assertEquals("CDB Caixa 2026", response.produtoValidado.nome);
    }

    @Test
    void deveLancarErroQuandoTipoProdutoInvalido() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "LCI";

        when(parametroProdutoRepository.findByTipo("LCI")).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simular(request);
        });

        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void deveLancarErroQuandoProdutoNaoEncontrado() {
        ParametroProduto parametro = new ParametroProduto();
        //parametro.setTipo("CDB");
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");

        when(parametroProdutoRepository.findByTipo("CDB")).thenReturn(parametro);
//        when(produtoRepository.findByTipoAndRisco("CDB", "Baixo")).thenReturn(Optional.empty());

        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "CDB";
        request.valor = 10000.0;
        request.prazoMeses = 12;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simular(request);
        });

        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void deveLancarErroQuandoValorAbaixoDoMinimo() {
        ParametroProduto parametro = new ParametroProduto();
        //parametro.setTipo("CDB");
        parametro.setMinValor(5000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");

        when(parametroProdutoRepository.findByTipo("CDB")).thenReturn(parametro);

        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "CDB";
        request.valor = 1000.0;
        request.prazoMeses = 12;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simular(request);
        });

        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    void deveLancarErroQuandoPrazoAcimaDoMaximo() {
        ParametroProduto parametro = new ParametroProduto();
        //parametro. setTipo("CDB");
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(12);
        parametro.setRiscoAceito("Baixo");

        when(parametroProdutoRepository.findByTipo("CDB")).thenReturn(parametro);

        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "CDB";
        request.valor = 10000.0;
        request.prazoMeses = 24;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simular(request);
        });

        assertEquals(422, ex.getResponse().getStatus());
    }
}
