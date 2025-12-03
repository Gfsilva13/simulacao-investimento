package br.gov.caixa.service;

import br.gov.caixa.dto.business.PerfilRiscoDTO;
import br.gov.caixa.dto.business.ProdutoDTO;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import br.gov.caixa.repository.business.ProdutoRepository;
import br.gov.caixa.service.business.PerfilRiscoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecomendacaoService {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    PerfilRiscoService perfilRiscoService;

    public List<ProdutoDTO> recomendarPorPerfil(String perfil){
        List<ProdutoInvestimento> produtos = produtoRepository.findByPerfil(perfil);
        return produtos.stream().map(ProdutoDTO :: fromEntity).
                collect(Collectors.toList());
    }

    public List<ProdutoDTO> recomendarPorCliente(Long clienteId) {
        PerfilRiscoDTO perfil = perfilRiscoService.perfilRisco(clienteId)
                .orElseThrow(() -> new WebApplicationException("Perfil não encontrado", 404));

        return recomendarPorPerfil(perfil.perfil);
    }
}
