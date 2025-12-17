package br.gov.caixa.controller.recomendacao;

import br.gov.caixa.dto.business.ProdutoDTO;
import br.gov.caixa.service.recomendacao.RecomendacaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/recomendacoes")
@SecurityRequirement(name = "SecurityScheme")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Recomendações")
public class RecomendacaoController {

    @Inject
    RecomendacaoService recomendacaoService;

    @GET
    @Path("/cliente/{id}")
    @RolesAllowed({"admin","user"})
    public List<ProdutoDTO> recomendacaoPorCliente(@PathParam("id") Long clienteId){
        return recomendacaoService.recomendarPorCliente(clienteId);
    }

    @GET
    @Path("/perfil/{perfil}")
    @RolesAllowed({"admin","user"})
    public List<ProdutoDTO> recomendacaoPorPerfil(@PathParam("perfil") String perfil){
        return recomendacaoService.recomendarPorPerfil(perfil);
    }
}
