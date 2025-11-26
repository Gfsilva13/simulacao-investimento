package br.gov.caixa.service;

import br.gov.caixa.dto.PerfilRiscoDTO;
import br.gov.caixa.entity.Cliente;
import br.gov.caixa.entity.Investimento;
import br.gov.caixa.repository.ClienteRepository;
import br.gov.caixa.repository.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PerfilRiscoService {

    @Inject
    InvestimentoRepository investimentoRepository;

    @Inject
    ClienteRepository clienteRepository;

    public Optional<PerfilRiscoDTO> calcularPerfil(Long clienteId){
        Cliente cliente = clienteRepository.findByOptional(clienteId)
                .orElseThrow(()-> new WebApplicationException("Cliente não encontrado", 404 ));
        List<Investimento> historico = investimentoRepository.findByCliente(clienteId);
        if (historico.isEmpty()){
            return Optional.of(new PerfilRiscoDTO(clienteId, "Conservador",
                    0, "Sem histórico, perfil padrão conservador."));
        }

        double volumeTotal = historico.stream()
                .mapToDouble(Investimento::getValor)
                .sum();

        int frequencia = historico.size();
        double prazoMedio = calcularPrazoMedio(historico);

        int pontuacao = calcularPontuacao(volumeTotal, frequencia, prazoMedio);
        String perfil = classificarPerfil(pontuacao);
        String descricao = descreverPerfil(perfil);

        return Optional.of(new PerfilRiscoDTO(clienteId, perfil, pontuacao, descricao));
    }

    private Double calcularPrazoMedio(List<Investimento> historico){
        LocalDateTime hoje = LocalDateTime.now();
        return historico.stream()
                .mapToDouble(i -> ChronoUnit.MONTHS.between(i.getData(), hoje))
                .average().orElse(0);
    }

    private int calcularPontuacao(double volume, int frequencia, double prazoMedio){
        int pontuacaoVolume = volume >= 10000 ? 40 : (int)(volume / 250);
        int pontuacaoFrequencia = frequencia >= 10 ? 30 : frequencia * 3;
        int pontuacaoPrazo = prazoMedio >= 12 ? 30 : (int)(prazoMedio * 2.5);
        return Math.min(pontuacaoVolume + pontuacaoFrequencia + pontuacaoPrazo, 100);
    }

    private String classificarPerfil(int pontuacao){
        if (pontuacao < 40) return "Conservador";
        else if (pontuacao < 70) return "Moderado";
        else return "Agressivo";
    }

    private String descreverPerfil(String perfil){
        return switch (perfil){
            case "Conservador" -> "Perfil com foco em segurança e liquidez.";
            case "Moderado" -> "Perfil equilibrado entre segurança e rentabilidade.";
            case "Agressivo" -> "Perfil voltado para alta rentabilidade e maior risco.";
            default -> "Perfil indefinido";
        };
    }
}
