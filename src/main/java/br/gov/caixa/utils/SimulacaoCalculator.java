package br.gov.caixa.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@ApplicationScoped
public class SimulacaoCalculator {

    public SimulacaoCalculator() {}

    public BigDecimal calcularValorFinal(@Min(value = 100, message = "O valor mínimo permitido é R$ 100") BigDecimal valorInicial, double rentabilidadeAnual, int prazoMeses) {
        double taxaMensal = Math.pow(1 + rentabilidadeAnual, 1.0 / 12) - 1;
        BigDecimal valor =
                BigDecimal.valueOf(1 + taxaMensal)
                        .pow(prazoMeses, new MathContext(10));
        return valorInicial.multiply(valor)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
