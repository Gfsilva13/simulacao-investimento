package br.gov.caixa.utils;

import static org.junit.jupiter.api.Assertions.*;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.entity.business.ParametroProduto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SimulacaoValidatorTest {

    private SimulacaoRequest request(double valor, int prazoMeses) {
        SimulacaoRequest r = new SimulacaoRequest();
        r.valor = valor;
        r.prazoMeses = prazoMeses;
        return r;
    }

    private ParametroProduto parametro(double minValor, int maxPrazo) {
        ParametroProduto p = new ParametroProduto();
        p.setMinValor(minValor);
        p.setMaxPrazo(maxPrazo);
        return p;
    }

    @Test
    void naoDeveLancarExcecaoQuandoValoresValidos() {
        SimulacaoRequest request = request(10_000.00, 12);
        ParametroProduto parametro = parametro(1_000.00, 24);

        assertDoesNotThrow(() ->
                SimulacaoValidator.validar(request, parametro)
        );
    }

    @Test
    void deveLancarErro422QuandoValorAbaixoDoMinimo() {
        SimulacaoRequest request = request(500.00, 12);
        ParametroProduto parametro = parametro(1_000.00, 24);

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> SimulacaoValidator.validar(request, parametro)
        );

        assertEquals(422, ex.getResponse().getStatus());
        assertEquals("Valor abaixo do mínimo permitido", ex.getMessage());
    }

    @Test
    void deveLancarErro422QuandoPrazoAcimaDoMaximo() {
        SimulacaoRequest request = request(10_000.00, 36);
        ParametroProduto parametro = parametro(1_000.00, 24);

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> SimulacaoValidator.validar(request, parametro)
        );

        assertEquals(422, ex.getResponse().getStatus());
        assertEquals("Prazo acima do máximo permitido", ex.getMessage());
    }
}

