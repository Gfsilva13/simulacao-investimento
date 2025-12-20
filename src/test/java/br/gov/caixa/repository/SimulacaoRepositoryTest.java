package br.gov.caixa.repository;

import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.entity.simulacao.Simulacao;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.repository.simulacao.SimulacaoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SimulacaoRepositoryTest {

    @Inject
    SimulacaoRepository simulacaoRepositoryTest;

    @Inject
    ClienteRepository clienteRepositoryTest;

    private Cliente clienteSimulacao1;
    private Cliente clienteSimulacao2;

    @BeforeEach
    @Transactional
    void setup() {
        ProdutoInvestimento produtoSimulacao1 = new ProdutoInvestimento();
        produtoSimulacao1.setId(1L);
        ProdutoInvestimento produtoSimulacao2 = new ProdutoInvestimento();
        produtoSimulacao2.setId(2L);

        clienteSimulacao1 = new Cliente();
        clienteSimulacao1.setNome("Cliente 1");
        clienteSimulacao1.setPerfilRisco("Conservador");
        clienteSimulacao1.setPontuacao(30);
        clienteRepositoryTest.persist(clienteSimulacao1);

        clienteSimulacao2 = new Cliente();
        clienteSimulacao2.setNome("Cliente 2");
        clienteSimulacao2.setPerfilRisco("Moderado");
        clienteSimulacao2.setPontuacao(45);
        clienteRepositoryTest.persist(clienteSimulacao2);

        Simulacao simulacaoTeste1 = new Simulacao();
        simulacaoTeste1.setCliente(clienteSimulacao1);
        simulacaoTeste1.setProduto(produtoSimulacao1);
        simulacaoTeste1.setDataSimulacao(LocalDateTime.now());
        simulacaoTeste1.setValorFinal(new BigDecimal("1100.00"));
        simulacaoTeste1.setPrazoMeses(12);
        simulacaoTeste1.setValorInvestido(new BigDecimal("1000.00"));
        simulacaoRepositoryTest.persist(simulacaoTeste1);

        Simulacao simulacaoTeste2 = new Simulacao();
        simulacaoTeste2.setCliente(clienteSimulacao1);
        simulacaoTeste2.setProduto(produtoSimulacao2);
        simulacaoTeste2.setDataSimulacao(LocalDateTime.now());
        simulacaoTeste2.setValorFinal(new BigDecimal("2200.00"));
        simulacaoTeste2.setPrazoMeses(24);
        simulacaoTeste2.setValorInvestido(new BigDecimal("2000.00"));
        simulacaoRepositoryTest.persist(simulacaoTeste2);

        Simulacao simulacaoTeste3 = new Simulacao();
        simulacaoTeste3.setCliente(clienteSimulacao2);
        simulacaoTeste3.setProduto(produtoSimulacao1);
        simulacaoTeste3.setDataSimulacao(LocalDateTime.now());
        simulacaoTeste3.setValorFinal(new BigDecimal("3300.00"));
        simulacaoTeste3.setPrazoMeses(36);
        simulacaoTeste3.setValorInvestido(new BigDecimal("3000.00"));
        simulacaoRepositoryTest.persist(simulacaoTeste3);
    }
    @Test
    void deveBuscarSimulacoesPorCliente() {
        List<Simulacao> resultadoSimulacao = simulacaoRepositoryTest.findByCliente(clienteSimulacao1.getId());
        assertEquals(2, resultadoSimulacao.size());
        resultadoSimulacao.forEach(i -> assertEquals(clienteSimulacao1.getId(), i.getCliente().getId()));
        assertTrue(resultadoSimulacao.stream().allMatch(s -> s.getCliente().getNome().equals("Cliente 1")));
    }

    @Test
    void deveBuscarPorProdutoENaData() {
        List<Simulacao> resultadoBusca =
                simulacaoRepositoryTest.findAllOrdered();

        assertEquals(3, resultadoBusca.size());
        assertEquals("Cliente 2", resultadoBusca.get(0).getCliente().getNome());
        assertTrue(
                resultadoBusca.get(0).getDataSimulacao()
                        .isAfter(resultadoBusca.get(1).getDataSimulacao())
        );
    }
}



