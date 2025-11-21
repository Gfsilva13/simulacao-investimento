package br.gov.caixa.controller;

import br.gov.caixa.dto.InvestimentoDTO;
import br.gov.caixa.service.InvestimentoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/investimentos")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Investimentos")
public class InvestimentoController {

    @Inject
    InvestimentoService investimentoService;

    @GET
    @Path("/{clienteId}")
    @RolesAllowed("user")
    public List<InvestimentoDTO> listar(@PathParam("clienteId") Long clienteId){
        return investimentoService.listarPorCliente(clienteId);
    }
}
