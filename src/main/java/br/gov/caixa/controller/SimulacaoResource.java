package br.gov.caixa.controller;

import br.gov.caixa.dto.SimulacaoHistoricoDTO;
import br.gov.caixa.dto.SimulacaoRequest;
import br.gov.caixa.dto.SimulacaoResponse;
import br.gov.caixa.dto.SimulacaoResumoPorProdutoDTO;
import br.gov.caixa.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "SecurityScheme")
@Path("/simular")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    @Inject
    SimulacaoService simulacaoService;

    @POST
    public SimulacaoResponse simular(SimulacaoRequest request) {
        return simulacaoService.simular(request);
    }

    @GET
    @Path("/historico")
    public List<SimulacaoHistoricoDTO> listarHistorico() {
        return simulacaoService.listarTodas();
    }

    @GET
    @Path("/resumo")
    public List<SimulacaoResumoPorProdutoDTO> resumoPorProdutoEDia() {
        return simulacaoService.resumoPorProdutoEDia();
    }
}
