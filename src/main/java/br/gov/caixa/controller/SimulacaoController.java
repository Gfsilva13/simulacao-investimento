package br.gov.caixa.controller;


import br.gov.caixa.dto.SimulacaoHistoricoDTO;
import br.gov.caixa.dto.SimulacaoRequest;
import br.gov.caixa.dto.SimulacaoResponse;
import br.gov.caixa.dto.SimulacaoResumoPorProdutoDTO;
import br.gov.caixa.service.SimulacaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@SecurityRequirement(name = "SecurityScheme")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name="simulações")
public class SimulacaoController {

    @Inject
    SimulacaoService simulacaoService;

    @POST
    @Path("/simular-investimento")
   // @RolesAllowed("user")
    public Response simularInvestimentoESalvar(@Valid SimulacaoRequest request){
        try {
            SimulacaoResponse response = simulacaoService.simular(request);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (WebApplicationException e){
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/simulacoes")
   // @RolesAllowed("user")
    public List<SimulacaoHistoricoDTO> listaSimulacoes(){
        return simulacaoService.listarTodas();
    }

    @GET
    @Path("/simulacoes/por-produto-dia")
   // @RolesAllowed("user")
    public List<SimulacaoResumoPorProdutoDTO> listarPorProdutoEDia(){
        return simulacaoService.resumoPorProdutoEDia();
    }
}
