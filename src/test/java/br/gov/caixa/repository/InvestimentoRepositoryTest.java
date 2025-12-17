package br.gov.caixa.repository;

import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.Investimento;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.repository.business.InvestimentoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class InvestimentoRepositoryTest{

    @Inject
    InvestimentoRepository investimentoRepository;

    @Inject
    ClienteRepository clienteRepository;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    @Transactional
    void setup() {
        investimentoRepository.deleteAll();
        clienteRepository.deleteAll();

        ProdutoInvestimento produto1 = new ProdutoInvestimento();
        produto1.setId(1L);

        ProdutoInvestimento produto2 = new ProdutoInvestimento();
        produto2.setId(2L);

        cliente1 = new Cliente();
        cliente1.setNome("Cliente 1");
        cliente1.setPerfilRisco("Conservador");
        cliente1.setPontuacao(30);
        clienteRepository.persist(cliente1);

        cliente2 = new Cliente();
        cliente2.setNome("Cliente 2");
        cliente2.setPerfilRisco("Moderado");
        cliente2.setPontuacao(45);
        clienteRepository.persist(cliente2);

        Investimento investimento1 = new Investimento();
        investimento1.setCliente(cliente1);
        investimento1.setProdutoInvestimento(produto1);
        investimento1.setValor(1000.00);
        investimento1.setRentabilidade(10.00);
        investimento1.setData(LocalDateTime.now());

        Investimento investimento2 = new Investimento();
        investimento2.setCliente(cliente2);
        investimento2.setProdutoInvestimento(produto2);
        investimento2.setValor(2000.00);
        investimento2.setRentabilidade(20.00);
        investimento2.setData(LocalDateTime.now());

        Investimento investimento3 = new Investimento();
        investimento3.setCliente(cliente2);
        investimento3.setProdutoInvestimento(produto1);
        investimento3.setRentabilidade(5.00);
        investimento3.setValor(500.00);
        investimento3.setData(LocalDateTime.now());

        investimentoRepository.persist(investimento1);
        investimentoRepository.persist(investimento2);
        investimentoRepository.persist(investimento3);
    }

    @Test
    void deveRetornarInvestimentosPorCliente() {
        List<Investimento> listaCliente1 = investimentoRepository.findByCliente(cliente2.getId());
        assertEquals(2, listaCliente1.size());
        listaCliente1.forEach(i -> assertEquals(cliente2.getId(), i.getCliente().getId()));
    }

    @Test
    void deveContarInvestimentosPorCliente() {
        long countCliente1 = investimentoRepository.countByCliente(cliente1.getId());
        long countCliente2 = investimentoRepository.countByCliente(cliente2.getId());

        assertEquals(1, countCliente1);
        assertEquals(2, countCliente2);
    }

    @Test
    void deveSomarTotalInvestidoPorCliente() {
        double totalCliente1 = investimentoRepository.totalInvestidoPorCliente(cliente1.getId());
        double totalCliente2 = investimentoRepository.totalInvestidoPorCliente(cliente2.getId());

        assertEquals(1000.00, totalCliente1, 0.001);
        assertEquals(2500.00, totalCliente2, 0.001);
    }
}

