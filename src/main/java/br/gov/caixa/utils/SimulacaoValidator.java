package br.gov.caixa.utils;

import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.entity.business.ParametroProduto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class SimulacaoValidator {

    public SimulacaoValidator() {}

    public static void validar(SimulacaoRequest request, ParametroProduto parametro) {
        if (request.valor.compareTo(parametro.getMinValor()) < 0) {
            throw new WebApplicationException("Valor abaixo do mínimo permitido", 422);
        }

        if (request.prazoMeses > parametro.getMaxPrazo()) {
            throw new WebApplicationException("Prazo acima do máximo permitido", 422);
        }
    }

}
