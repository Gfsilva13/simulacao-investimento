package br.gov.caixa.service.business;

import br.gov.caixa.dto.business.InvestimentoDTO;
import br.gov.caixa.repository.business.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InvestimentoService {

    @Inject
    InvestimentoRepository investimentoRepository;

    public List<InvestimentoDTO> listarPorCliente (Long clienteId){
        return investimentoRepository.findByCliente(clienteId).stream()
                .map(InvestimentoDTO:: fromEntity)
                .collect(Collectors.toList());
    }
}
