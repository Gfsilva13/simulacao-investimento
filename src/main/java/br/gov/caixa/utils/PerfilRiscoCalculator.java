package br.gov.caixa.utils;

import br.gov.caixa.entity.business.Investimento;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class PerfilRiscoCalculator {

    public int calcularPontuacao(List<Investimento> historico) {
        if (historico == null || historico.isEmpty()) {
            return 0;
        }
        BigDecimal volume = historico.stream().map(Investimento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int frequencia = historico.size();
        double prazoMedio = historico.stream()
                .mapToDouble(Investimento::getPrazoMeses)
                .average()
                .orElse(0.0);
        int pVolume = volume.compareTo(new BigDecimal("10000")) >= 0
                ? 40 : volume.divide(new BigDecimal("250"), RoundingMode.DOWN).intValue();

        int pFreq = frequencia >= 10 ? 30 : frequencia * 3;
        int pPrazo = prazoMedio >= 12 ? 30 : (int)Math.floor(prazoMedio * 2.5);

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
