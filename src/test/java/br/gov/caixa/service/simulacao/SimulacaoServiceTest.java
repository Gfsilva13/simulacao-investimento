package br.gov.caixa.service.simulacao;

import br.gov.caixa.dto.simulacao.SimulacaoHistoricoDTO;
import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.dto.simulacao.SimulacaoResponse;
import br.gov.caixa.dto.simulacao.SimulacaoResumoPorProdutoDTO;
import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.entity.simulacao.Simulacao;
import br.gov.caixa.repository.business.ParametroProdutoRepository;
import br.gov.caixa.repository.business.ProdutoRepository;
import br.gov.caixa.repository.simulacao.SimulacaoRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class SimulacaoServiceTest {

    @Inject
    SimulacaoService simulacaoService;

    @InjectMock
    ParametroProdutoRepository parametroProdutoRepository;

    @InjectMock
    ProdutoRepository produtoRepository;

    @InjectMock
    SimulacaoRepository simulacaoRepository;

    private ParametroProduto parametro(BigDecimal min, int maxPrazo, String risco) {
        ParametroProduto p = new ParametroProduto();
        p.setMinValor(min);
        p.setMaxPrazo(maxPrazo);
        p.setRiscoAceito(risco);
        return p;
    }

    private ProdutoInvestimento produto(Long id, String nome, String tipo, double rentabilidade) {
        ProdutoInvestimento p = new ProdutoInvestimento();
        p.setId(id);
        p.setNomeProduto(nome);
        p.setTipoProduto(tipo);
        p.setRentabilidade(rentabilidade);
        return p;
    }

    private Simulacao simulacao(Long id, ProdutoInvestimento produto, BigDecimal valorFinal, LocalDateTime dataSimulacao) {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        Simulacao s = new Simulacao();
        s.setId(id);
        s.setCliente(cliente);
        s.setProduto(produto);
        s.setValorFinal(valorFinal);
        s.setDataSimulacao(dataSimulacao);
        return s;
    }

    SimulacaoRequest request(String tipoProduto, @Min(value = 100, message = "O valor mínimo permitido é R$ 100") BigDecimal valor, int prazoMeses) {
        SimulacaoRequest r = new SimulacaoRequest();
        r.tipoProduto = tipoProduto;
        r.valor = valor;
        r.prazoMeses = prazoMeses;
        r.clienteId = 123L;
        return r;
    }

    private void assertErro404(Runnable executavel) {
        WebApplicationException ex = assertThrows(WebApplicationException.class, executavel::run);
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void deveSimularInvestimentoComProdutoValido() {
        ParametroProduto parametro = parametro(BigDecimal.valueOf(1000.00), 24, "Baixo");
        ProdutoInvestimento produto = produto(101L, "CDB Caixa 2026", "CDB", 0.12);
        produto.setParametroProduto(parametro);

        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro);
        when(produtoRepository.findByTipo("CDB")).thenReturn(Optional.of(produto));

        SimulacaoResponse response = simulacaoService.simularInvestimento(
                request("CDB", BigDecimal.valueOf(10000.00), 12)
        );

        //assertEquals(11200.00, response.resultadoSimulacao.valorFinal, String.valueOf(0.01));
        assertEquals("CDB Caixa 2026", response.produtoValidado.nome);
    }

    @Test
    void deveLancarErroQuandoTipoProdutoInvalido() {
        when(parametroProdutoRepository.findByRisco("LCI")).thenReturn(null);

        assertErro404(() -> simulacaoService.simularInvestimento(request("LCI", BigDecimal.valueOf(1000.00), 12)));
    }

    @Test
    void deveLancarErroQuandoProdutoNaoEncontrado() {
        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro(BigDecimal.valueOf(1000), 24, "Baixo"));
        when(produtoRepository.findByTipo("CDB")).thenReturn(Optional.empty());

        assertErro404(() -> simulacaoService.simularInvestimento(request("CDB", BigDecimal.valueOf(10000.00), 12)));
    }

    @Test
    void deveLancarErroQuandoValorAbaixoDoMinimo() {
        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro(BigDecimal.valueOf(5000.00), 24, "Baixo"));

        assertErro404(() -> simulacaoService.simularInvestimento(request("CDB", BigDecimal.valueOf(1000.00), 12)));
    }

    @Test
    void deveLancarErroQuandoPrazoAcimaDoMaximo() {
        when(parametroProdutoRepository.findByRisco("CDB")).thenReturn(parametro(BigDecimal.valueOf(1000.00), 12, "Baixo"));

        assertErro404(() -> simulacaoService.simularInvestimento(request("CDB", BigDecimal.valueOf(10000.00), 24)));
    }

    @Test
    void deveListarTodasAsSimulacoesTransformandoParaDTO() {
        ProdutoInvestimento produto1 = produto(1L, "CDB", "CDB", 0.1);
        ProdutoInvestimento produto2 = produto(2L, "LCI", "LCI", 0.08);

        Simulacao sim1 = simulacao(1L, produto1, BigDecimal.valueOf(100.00), LocalDateTime.now());
        Simulacao sim2 = simulacao(2L, produto2, BigDecimal.valueOf(200.00), LocalDateTime.now());

        when(simulacaoRepository.findAllOrdered()).thenReturn(List.of(sim1, sim2));

        List<SimulacaoHistoricoDTO> resultado = simulacaoService.listarTodas();

        assertEquals(2, resultado.size());
        assertEquals(sim1.getId(), resultado.get(0).id);
        assertEquals(sim2.getId(), resultado.get(1).id);

        verify(simulacaoRepository, times(1)).findAllOrdered();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverSimulacoes() {
        when(simulacaoRepository.findAllOrdered()).thenReturn(List.of());

        List<SimulacaoHistoricoDTO> resultado = simulacaoService.listarTodas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(simulacaoRepository, times(1)).findAllOrdered();
    }

    @Test
    void deveGerarResumoPorProdutoEDia() {
        ProdutoInvestimento cdb = produto(1L, "CDB", "CDB", 0.1);
        ProdutoInvestimento lci = produto(2L, "LCI", "LCI", 0.1);

        Simulacao sim1 = simulacao(1L, cdb, BigDecimal.valueOf(100.00), LocalDateTime.of(2025, 11, 10, 10, 0));
        Simulacao sim2 = simulacao(2L, cdb, BigDecimal.valueOf(200.00), LocalDateTime.of(2025, 11, 10, 11, 0));
        Simulacao sim3 = simulacao(3L, lci, BigDecimal.valueOf(300.00), LocalDateTime.of(2025, 11, 11, 12, 0));

        when(simulacaoRepository.listAll()).thenReturn(List.of(sim1, sim2, sim3));

        List<SimulacaoResumoPorProdutoDTO> resultado = simulacaoService.resumoPorProdutoEDia();

        Map<String, SimulacaoResumoPorProdutoDTO> mapa = resultado.stream()
                .collect(Collectors.toMap(r -> r.produto + "_" + r.data, r -> r));

        SimulacaoResumoPorProdutoDTO cdb10 = mapa.get("CDB_2025-11-10");
        assertNotNull(cdb10);
        assertEquals(2, cdb10.quantidadeSimulacoes);
        //assertEquals(150.00, cdb10.mediaValorFinal, String.valueOf(0.0001));

        SimulacaoResumoPorProdutoDTO lci11 = mapa.get("LCI_2025-11-11");
        assertNotNull(lci11);
        assertEquals(1, lci11.quantidadeSimulacoes);

        //assertEquals(300.00, lci11.mediaValorFinal, String.valueOf(0.0001));

        verify(simulacaoRepository, times(1)).listAll();
    }

    @Test
    void deveBuscarSimulacoesPorCliente() {
        ProdutoInvestimento produto1 = produto(1L, "CDB", "CDB", 0.1);
        ProdutoInvestimento produto2 = produto(2L, "LCI", "LCI", 0.08);

        Simulacao sim1 = simulacao(1L, produto1, BigDecimal.valueOf(100.00), LocalDateTime.now());
        Simulacao sim2 = simulacao(2L, produto2, BigDecimal.valueOf(200.00), LocalDateTime.now());
        Simulacao sim3 = simulacao(1L, produto2, BigDecimal.valueOf(300.00), LocalDateTime.now());

        when(simulacaoRepository.findByCliente(1L)).thenReturn(List.of(sim1,sim3));

        List<SimulacaoHistoricoDTO> resultadoSimulacao = simulacaoService.listarSimulacaoCliente(1l);
        assertEquals(2, resultadoSimulacao.size());
        assertTrue(resultadoSimulacao.stream().allMatch(s -> s.clienteId.equals(1L)));
    }
}
