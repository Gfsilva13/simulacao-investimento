package br.gov.caixa.controller;

import br.gov.caixa.controller.simulacao.SimulacaoController;
import br.gov.caixa.dto.business.ProdutoDTO;
import br.gov.caixa.dto.simulacao.*;
import br.gov.caixa.service.simulacao.SimulacaoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import io.quarkus.test.security.TestSecurity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SimulacaoControllerTest {

    @Inject
    SimulacaoController simulacaoController;

    @InjectMock
    SimulacaoService simulacaoService;

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    public void testSimularInvestimentoESalvar_sucesso() {
        SimulacaoRequest request = new SimulacaoRequest();
        ProdutoDTO produto = new ProdutoDTO();
        produto.nome = "Produto_Teste";
        produto.tipo = "LCI";
        produto.risco="BAIXO";
        produto.rentabilidade=1.0;

        request.clienteId= 1L;
        request.valor=1000.00;
        request.prazoMeses=36;
        request.tipoProduto="LCI";

        ResultadoSimulacaoDTO resultado =
                new ResultadoSimulacaoDTO(1000.00, 1.0, 36);

        SimulacaoResponse simulacaoResponse = new SimulacaoResponse();
        simulacaoResponse.produtoValidado = produto;
        simulacaoResponse.resultadoSimulacao = resultado;
        simulacaoResponse.dataSimulacao = LocalDateTime.now();

        when(simulacaoService.simularInvestimento(request)).thenReturn(simulacaoResponse);
        Response response = simulacaoController.simularInvestimentoESalvar(request);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        SimulacaoResponse entity = (SimulacaoResponse) response.getEntity();
        assertNotNull(entity);
        assertEquals("Produto_Teste", entity.produtoValidado.nome);
        assertEquals(1000.0, entity.resultadoSimulacao.valorFinal);
        assertNotNull(entity.dataSimulacao);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    public void testSimularInvestimentoESalvar_erro() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId= 1L;
        request.valor=1000.00;
        request.prazoMeses=1;
        request.tipoProduto="produto";

        WebApplicationException exception = new WebApplicationException(Response.status(400).entity("HTTP 400 Bad Request").build());
        when(simulacaoService.simularInvestimento(request)).thenThrow(exception);

        Response response = simulacaoController.simularInvestimentoESalvar(request);

        assertEquals(400, response.getStatus());
        assertEquals("HTTP 400 Bad Request", response.getEntity());

        verify(simulacaoService).simularInvestimento(request);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    public void testListarHistorico() {
        List<SimulacaoHistoricoDTO> historico = List.of(new SimulacaoHistoricoDTO());
        when(simulacaoService.listarTodas()).thenReturn(historico);
        List<SimulacaoHistoricoDTO> result = simulacaoController.listarHistorico();
        assertEquals(historico, result);
        verify(simulacaoService).listarTodas();
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    public void testListarPorProdutoEDia() {
        SimulacaoResumoPorProdutoDTO simulacao = new SimulacaoResumoPorProdutoDTO(
                "produto_Teste", LocalDate.now(), 10, 1.0);

        List<SimulacaoResumoPorProdutoDTO> resumo = List.of(simulacao);
        when(simulacaoService.resumoPorProdutoEDia()).thenReturn(resumo);
        List<SimulacaoResumoPorProdutoDTO> result = simulacaoController.listarPorProdutoEDia();
        assertEquals(resumo, result);
        verify(simulacaoService).resumoPorProdutoEDia();
    }
}

