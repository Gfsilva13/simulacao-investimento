package br.gov.caixa.service;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.dto.simulacao.SimulacaoResponse;
import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.ParametroProdutoRepository;
import br.gov.caixa.repository.business.ProdutoRepository;
import br.gov.caixa.repository.simulacao.SimulacaoRepository;
import br.gov.caixa.service.simulacao.SimulacaoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
@Testcontainers
@QuarkusTestResource(SqlServerTestResource.class)
public class SimulacaoServiceTest {

    @Inject
    SimulacaoService simulacaoService;

    @InjectMock
    ParametroProdutoRepository parametroProdutoRepository;

    @InjectMock
    ProdutoRepository produtoRepository;

    @InjectMock
    SimulacaoRepository simulacaoRepository;

    @Test
    void deveSimularInvestimentoInvestimentoComProdutoValido() {
        ParametroProduto parametro = new ParametroProduto();
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");

        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setId(101L);
        produto.setNomeProduto("CDB Caixa 2026");
        produto.setTipoProduto("CDB");
        produto.setRentabilidade(0.12);
        produto.setParametroProduto(parametro);

        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro);
        when(produtoRepository.findByTipo("CDB"))
                .thenReturn(Optional.of(produto));

        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId = 123L;
        request.valor = 10000.0;
        request.prazoMeses = 12;
        request.tipoProduto = "CDB";

        SimulacaoResponse response = simulacaoService.simularInvestimento(request);

        assertEquals(11200.0, response.resultadoSimulacao.valorFinal, 0.01);
        assertEquals("CDB Caixa 2026", response.produtoValidado.nome);
    }

    @Test
    void deveLancarErroQuandoTipoProdutoInvalido() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "LCI";

        when(parametroProdutoRepository.findByRisco("LCI")).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simularInvestimento(request);
        });

        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void deveLancarErroQuandoProdutoNaoEncontrado() {
        ParametroProduto parametro = new ParametroProduto();
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");

        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro);
        when(produtoRepository.findByTipo("CDB")).thenReturn(Optional.empty());

        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "CDB";
        request.valor = 10000.0;
        request.prazoMeses = 12;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simularInvestimento(request);
        });

        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void deveLancarErroQuandoValorAbaixoDoMinimo() {
        ParametroProduto parametro = new ParametroProduto();
        parametro.setMinValor(5000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");

        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro);

        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "CDB";
        request.valor = 1000.0;
        request.prazoMeses = 12;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simularInvestimento(request);
        });

        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void deveLancarErroQuandoPrazoAcimaDoMaximo() {
        ParametroProduto parametro = new ParametroProduto();
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(12);
        parametro.setRiscoAceito("Baixo");

        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro);

        SimulacaoRequest request = new SimulacaoRequest();
        request.tipoProduto = "CDB";
        request.valor = 10000.0;
        request.prazoMeses = 24;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            simulacaoService.simularInvestimento(request);
        });

        assertEquals(404, ex.getResponse().getStatus());
    }
}
