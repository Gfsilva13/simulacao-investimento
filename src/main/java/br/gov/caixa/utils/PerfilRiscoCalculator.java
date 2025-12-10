package br.gov.caixa.utils;

import br.gov.caixa.entity.business.Investimento;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PerfilRiscoCalculator {

    public int calcularPontuacao(List<Investimento> historico) {
        if (historico == null || historico.isEmpty()) {
            return 0;
        }
        double volume = historico.stream().mapToDouble(Investimento::getValor).sum();
        int frequencia = historico.size();
        double prazoMedio = historico.stream()
                .mapToDouble(i -> i.getPrazoMeses())
                .average()
                .orElse(0);

        int pVolume = volume >= 10000 ? 40 : (int)(volume / 250);
        int pFreq = frequencia >= 10 ? 30 : frequencia * 3;
        int pPrazo = prazoMedio >= 12 ? 30 : (int)(prazoMedio * 2.5);

        return Math.min(pVolume + pFreq + pPrazo, 100);
    }

    public String classificarPerfil(int pontuacao) {
        if (pontuacao >= 70) return "Agressivo";
        if (pontuacao >= 40) return "Moderado";
        return "Conservador";
    }

    public String descreverPerfil(String perfil) {
        return switch (perfil) {
            case "Agressivo" -> "Perfil voltado para alta rentabilidade com maior risco";
            case "Moderado" -> "Perfil equilibrado entre risco e retorno";
            default -> "Perfil conservador com foco em segurança";
        };
    }

}
