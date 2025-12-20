package br.gov.caixa.service.businnes;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PerfilRiscoServiceLambdaTest {

    @Inject
    PerfilRiscoService perfilRiscoService;
    @InjectMock
    InvestimentoRepository investimentoRepository;
    @InjectMock
    ClienteRepository clienteRepository;
    @InjectMock
    PerfilRiscoCalculator perfilRiscoCalculator;


    private Investimento criarInvestimento(BigDecimal valor, int prazoMeses) {
        Investimento investimento = new Investimento();
        investimento.setValor(valor);
        investimento.setPrazoMeses(prazoMeses);
        investimento.setData(LocalDateTime.now().minusMonths(prazoMeses));
        ChronoUnit.MONTHS.between(investimento.getData(), LocalDateTime.now());
        return investimento;
    }

    @Test
    public void deveAcionarLambdaDoCalculoDeRisco() {
        Long clienteId = 99L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        List<Investimento> historico = List.of(
                criarInvestimento(BigDecimal.valueOf(2000.0), 6),
                criarInvestimento(BigDecimal.valueOf(3000.0), 9),
                criarInvestimento(BigDecimal.valueOf(4500.0), 12)
        );
        when(clienteRepository.findByOptional(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(investimentoRepository.findByCliente(clienteId)).thenReturn(historico);
        when(perfilRiscoCalculator.calcularPontuacao(any()))
                .thenReturn(42);
        when(perfilRiscoCalculator.classificarPerfil(anyInt()))
                .thenReturn("Moderado");
        when(perfilRiscoCalculator.descreverPerfil(anyString()))
                .thenReturn("Perfil equilibrado.");

        PerfilRiscoDTO perfil = perfilRiscoService.perfilRisco(clienteId)
                .orElseThrow();
        assertEquals(clienteId, perfil.clienteId);
        assertNotNull(perfil.perfil);
        assertTrue(perfil.pontuacao > 0);
        assertNotNull(perfil.descricao);
    }
}
