package br.gov.caixa.repository;

import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.repository.business.ClienteRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ClienteRepositoryTest {

    @Inject
    ClienteRepository clienteRepository;

    @Test
    @Transactional
    void deveRetornarClienteQuandoIdExistir() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente");
        cliente.setPerfilRisco("Conservador");
        cliente.setPontuacao(10);
        clienteRepository.persist(cliente);

        Long id = cliente.getId();
        assertNotNull(id);

        Optional<Cliente> resultado = clienteRepository.findByOptional(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
    }

    @Test
    void deveRetornarOptionalVazioQuandoIdNaoExistir() {
        Optional<Cliente> resultadoCliente = clienteRepository.findByOptional(99L);
        assertTrue(resultadoCliente.isEmpty());
    }
}


