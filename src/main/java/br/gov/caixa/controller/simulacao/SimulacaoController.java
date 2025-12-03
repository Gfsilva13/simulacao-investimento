package br.gov.caixa.controller.simulacao;


import br.gov.caixa.dto.simulacao.SimulacaoHistoricoDTO;
import br.gov.caixa.dto.simulacao.SimulacaoRequest;
import br.gov.caixa.dto.simulacao.SimulacaoResponse;
import br.gov.caixa.dto.simulacao.SimulacaoResumoPorProdutoDTO;
import br.gov.caixa.service.simulacao.SimulacaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/simular-investimento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name="Simulações")
public class SimulacaoController {

    @Inject
    SimulacaoService simulacaoService;

    @POST
    @Path("/")
    @RolesAllowed({"admin","user"})
    public Response simularInvestimentoESalvar(@Valid SimulacaoRequest request){
       try {
            SimulacaoResponse response = simulacaoService.simularInvestimento(request);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (WebApplicationException e){
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/historico")
    @RolesAllowed({"admin","user"})
    public List<SimulacaoHistoricoDTO> listarHistorico() {
        return simulacaoService.listarTodas();
    }

    @GET
    @Path("/simulacoes/por-produto-dia")
    @RolesAllowed({"admin","user"})
    public List<SimulacaoResumoPorProdutoDTO> listarPorProdutoEDia(){
        return simulacaoService.resumoPorProdutoEDia();
    }
}
