package br.gov.caixa.util;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulacaoUtils {

    public SimulacaoUtils() {}

    public static double calcularValorFinal(double valorInicial, double rentabilidadeAnual, int prazoMeses) {
        double taxaMensal = Math.pow(1 + rentabilidadeAnual, 1.0 / 12) - 1;
        return valorInicial * Math.pow(1 + taxaMensal, prazoMeses);
    }
}
