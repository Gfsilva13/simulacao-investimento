package br.gov.caixa.service.business;

import br.gov.caixa.telemetria.MedirTelemetria;
import br.gov.caixa.utils.PerfilRiscoCalculator;
import br.gov.caixa.dto.business.PerfilRiscoDTO;
import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.Investimento;
import br.gov.caixa.repository.business.ClienteRepository;
import br.gov.caixa.repository.business.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PerfilRiscoService {

    @Inject
    PerfilRiscoCalculator perfilRiscoCalculator;
    @Inject
    InvestimentoRepository investimentoRepository;
    @Inject
    ClienteRepository clienteRepository;

    @MedirTelemetria
    public Optional<PerfilRiscoDTO> perfilRisco(Long clienteId) {
        Cliente cliente = clienteRepository.findByOptional(clienteId)
                .orElseThrow(() -> new WebApplicationException("Cliente não encontrado", 404));
        List<Investimento> historico = investimentoRepository.findByCliente(clienteId);
        if (historico.isEmpty()) {
            return Optional.of(new PerfilRiscoDTO(clienteId, "Conservador",
                    0, "Sem histórico, perfil padrão conservador."));
        }

        int pontuacao = perfilRiscoCalculator.calcularPontuacao(historico);
        String perfil = perfilRiscoCalculator.classificarPerfil(pontuacao);
        String descricao = perfilRiscoCalculator.descreverPerfil(perfil);

        return Optional.of(new PerfilRiscoDTO(clienteId, perfil, pontuacao, descricao));
    }

    public Optional<PerfilRiscoDTO> perfilCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findByOptional(clienteId)
                .orElseThrow(() -> new WebApplicationException("Cliente não encontrado", 404));
        return Optional.of(new PerfilRiscoDTO(cliente.getId(), cliente.getPerfilRisco(),
                cliente.getPontuacao(), "Perfil Cliente"));
        }
}
