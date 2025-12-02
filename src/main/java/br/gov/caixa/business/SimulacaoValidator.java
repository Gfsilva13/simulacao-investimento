package br.gov.caixa.business;

import br.gov.caixa.dto.SimulacaoRequest;
import br.gov.caixa.entity.ParametroProduto;
import br.gov.caixa.entity.ProdutoInvestimento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

import java.util.Optional;

@ApplicationScoped
public class SimulacaoValidator {

    public SimulacaoValidator() {}

    public static void validar(SimulacaoRequest request, ParametroProduto parametro) {
        if (request.valor < parametro.getMinValor()) {
            throw new WebApplicationException("Valor abaixo do mínimo permitido", 422);
        }

        if (request.prazoMeses > parametro.getMaxPrazo()) {
            throw new WebApplicationException("Prazo acima do máximo permitido", 422);
        }
    }

}
