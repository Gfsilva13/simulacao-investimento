package br.gov.caixa.service.simulacao;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.dto.simulacao.SimulacaoResponse;
import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.ParametroProduto;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.entity.simulacao.Simulacao;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.repository.business.ParametroProdutoRepository;
import br.gov.caixa.repository.business.ProdutoRepository;
import br.gov.caixa.repository.simulacao.SimulacaoRepository;

import br.gov.caixa.service.SqlServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.transaction.Transactional;
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

    @Inject
    ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    @Transactional
    void setup() {
        simulacaoRepository.deleteAll();

        cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setPontuacao(10);
        cliente.setPerfilRisco("Conservador");
        clienteRepository.persist(cliente);

        ParametroProduto parametro = new ParametroProduto();
        parametro.setMinValor(1000.0);
        parametro.setMaxPrazo(24);
        parametro.setRiscoAceito("Baixo");
        parametroProdutoRepository.persist(parametro);

        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setTipoProduto("CDB");
        produto.setRentabilidade(0.12);
        produto.setNomeProduto("CDB Real");
        produto.setParametroProduto(parametro);
        produtoRepository.persist(produto);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void deveExecutarSimulacaoComSQLServer() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.clienteId = cliente.getId();
        request.valor = 10000.0;
        request.prazoMeses = 12;
        request.tipoProduto = "CDB";
        SimulacaoResponse response = simulacaoService.simularInvestimento(request);

        List<Simulacao> simulacoes = simulacaoRepository.listAll();
        Assertions.assertEquals(1, simulacoes.size());
        Assertions.assertEquals(11200.0, simulacoes.get(0).getValorFinal(), 0.01);
    }
}