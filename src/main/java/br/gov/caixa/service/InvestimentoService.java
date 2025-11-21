package br.gov.caixa.service;

import br.gov.caixa.dto.InvestimentoDTO;
import br.gov.caixa.repository.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InvestimentoService {

    @Inject
    InvestimentoRepository investimentoRepository;

    public List<InvestimentoDTO> listarPorCliente (Long clienteID){
        return investimentoRepository.findByCliente(clienteID).stream()
                .map(InvestimentoDTO:: fromEntity)
                .collect(Collectors.toList());
    }
}
