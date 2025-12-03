package br.gov.caixa.utils;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulacaoCalculator {

    public SimulacaoCalculator() {}

    public static double calcularValorFinal(double valorInicial, double rentabilidadeAnual, int prazoMeses) {
        double taxaMensal = Math.pow(1 + rentabilidadeAnual, 1.0 / 12) - 1;
        return valorInicial * Math.pow(1 + taxaMensal, prazoMeses);
    }
}
