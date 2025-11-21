package br.gov.caixa.service;

import br.gov.caixa.dto.SimulacaoRequest;
import br.gov.caixa.dto.SimulacaoResponse;
import br.gov.caixa.entity.ParametroProduto;
import br.gov.caixa.entity.ProdutoInvestimento;
import br.gov.caixa.entity.Simulacao;
import br.gov.caixa.repository.ParametroProdutoRepository;
import br.gov.caixa.repository.ProdutoRepository;
import br.gov.caixa.repository.SimulacaoRepository;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.inject.Inject;
import java.util.*;

@Testcontainers
@QuarkusTest
@QuarkusTestResource(SqlServerTestResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimulacaoServiceIntegrationTest {

    @Inject
    SimulacaoService simulacaoService;

    @Inject
    ParametroProdutoRepository parametroProdutoRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    SimulacaoRepository simulacaoRepository;

    @BeforeEach
    void setup() {
        ParametroProduto parametro = new ParametroProduto();
        parametro.setTipo("CDB");
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");
        parametroProdutoRepository.persist(parametro);

        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setTipoProduto("CDB");
        produto.setRentabilidade(0.12);
        produto.setRisco("Baixo");
        produto.setNome("CDB Real");
        produtoRepository.persist(produto);
    }

    @Test
    void deveExecutarSimulacaoComSQLServer() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId = 99L;
        request.valor = 10000.0;
        request.prazoMeses = 12;
        request.tipoProduto = "CDB";

        SimulacaoResponse response = simulacaoService.simular(request);

        List<Simulacao> simulacoes = simulacaoRepository.listAll();
        Assertions.assertEquals(1, simulacoes.size());
        Assertions.assertEquals(11200.0, simulacoes.get(0).getValorFinal(), 0.01);
    }
}