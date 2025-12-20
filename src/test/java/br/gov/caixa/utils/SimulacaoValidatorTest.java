package br.gov.caixa.utils;

import static org.junit.jupiter.api.Assertions.*;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.entity.business.ParametroProduto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
class SimulacaoValidatorTest {

    SimulacaoRequest request(@Min(value = 100, message = "O valor mínimo permitido é R$ 100") BigDecimal valor, int prazoMeses) {
        SimulacaoRequest r = new SimulacaoRequest();
        r.valor = valor;
        r.prazoMeses = prazoMeses;
        return r;
    }

    ParametroProduto parametro(BigDecimal minValor, int maxPrazo) {
        ParametroProduto p = new ParametroProduto();
        p.setMinValor(minValor);
        p.setMaxPrazo(maxPrazo);
        return p;
    }

    @Test
    void naoDeveLancarExcecaoQuandoValoresValidos() {
        SimulacaoRequest request = request(BigDecimal.valueOf(10_000.00), 12);
        ParametroProduto parametro = parametro(BigDecimal.valueOf(1_000.00), 24);

        assertDoesNotThrow(() ->
                SimulacaoValidator.validar(request, parametro)
        );
    }

    @Test
    void deveLancarErro422QuandoValorAbaixoDoMinimo() {
        SimulacaoRequest request = request(BigDecimal.valueOf(500.00), 12);
        ParametroProduto parametro = parametro(BigDecimal.valueOf(1_000.00), 24);

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> SimulacaoValidator.validar(request, parametro)
        );

        assertEquals(422, ex.getResponse().getStatus());
        assertEquals("Valor abaixo do mínimo permitido", ex.getMessage());
    }

    @Test
    void deveLancarErro422QuandoPrazoAcimaDoMaximo() {
        SimulacaoRequest request = request(BigDecimal.valueOf(10_000.00), 36);
        ParametroProduto parametro = parametro(BigDecimal.valueOf(1_000.00), 24);

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> SimulacaoValidator.validar(request, parametro)
        );

        assertEquals(422, ex.getResponse().getStatus());
        assertEquals("Prazo acima do máximo permitido", ex.getMessage());
    }
}

