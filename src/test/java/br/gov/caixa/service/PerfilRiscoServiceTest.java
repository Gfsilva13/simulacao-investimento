package br.gov.caixa.service;

import br.gov.caixa.dto.business.PerfilRiscoDTO;
import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.Investimento;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.repository.business.InvestimentoRepository;
import br.gov.caixa.service.business.PerfilRiscoService;
import br.gov.caixa.utils.PerfilRiscoCalculator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PerfilRiscoServiceTest {

    @Inject
    PerfilRiscoService perfilRiscoService;

    @InjectMock
    InvestimentoRepository investimentoRepository;

    @InjectMock
    ClienteRepository clienteRepository;

    @Mock
    PerfilRiscoCalculator perfilRiscoCalculator;

    private Investimento investimento(double valor, int prazoMeses) {
        Investimento investimento = new Investimento();
        investimento.setValor(valor);
        investimento.setPrazoMeses(prazoMeses);
        investimento.setData(LocalDateTime.now().minusMonths(prazoMeses));
        ChronoUnit.MONTHS.between(investimento.getData(), LocalDateTime.now());
        return investimento;
    }

    @Test
    void deveRetornarPerfilRiscoModeradoComHistoricoMedio() {
        Long clienteId = 1L;

        List<Investimento> historico = List.of(
                investimento(2000.0, 6),
                investimento(3000.0, 9),
                investimento(4500.0, 12)
        );

        when(clienteRepository.findByOptional(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(historico);

        PerfilRiscoDTO perfil = perfilRiscoService.perfilRisco(clienteId).orElseThrow();

        assertEquals(clienteId, perfil.clienteId);
        assertEquals("Moderado", perfil.perfil);
        assertTrue(perfil.pontuacao >= 40 && perfil.pontuacao < 70);
        assertNotNull(perfil.descricao);
    }

    @Test
    void deveRetornarPerfilConservadorComHistoricoFraco() {
        Long clienteId = 2L;

        List<Investimento> historico = List.of(
                investimento(1500.0, 3),
                investimento(2200.0, 2),
                investimento(2500.0, 1)
        );

        when(clienteRepository.findByOptional(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(historico);

        PerfilRiscoDTO perfil = perfilRiscoService.perfilRisco(clienteId).orElseThrow();

        assertEquals("Conservador", perfil.perfil);
        assertTrue(perfil.pontuacao < 40);
    }

    @Test
    void deveRetornarPerfilAgressivoComHistoricoForte() {
        Long clienteId = 3L;

        List<Investimento> historico = List.of(
                investimento(15000.0, 18),
                investimento(12000.0, 24),
                investimento(10000.0, 12)
        );

        when(clienteRepository.findByOptional(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(historico);

        PerfilRiscoDTO perfil = perfilRiscoService.perfilRisco(clienteId).orElseThrow();
        System.out.println("Pontuação calculada: " + perfil.pontuacao);
        assertEquals("Agressivo", perfil.perfil);
        assertTrue(perfil.pontuacao >= 70);
    }

    @Test
    void deveRetornarPerfilPadraoQuandoNaoHaInvestimentos() {
        Long clienteId = 4L;

        when(clienteRepository.findByOptional(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(Collections.emptyList());

        PerfilRiscoDTO perfil = perfilRiscoService.perfilRisco(clienteId).orElseThrow();

        assertEquals("Conservador", perfil.perfil);
        assertEquals(0, perfil.pontuacao);
        assertTrue(perfil.descricao.contains("perfil padrão"));
    }
}
