package br.gov.caixa.controller.business;

import br.gov.caixa.service.business.InvestimentoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Optional;

@Path("/investimento-cliente")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Investimentos Financeiros")
public class InvestimentoController {

    @Inject
    InvestimentoService investimentoService;

    @GET
    @Path("/{clienteId}")
    @RolesAllowed({"admin", "user"})
    public Response listar(@PathParam("clienteId") Long clienteId){
        if (clienteId == null || clienteId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O ID do cliente deve ser um número positivo.")
                    .build();
        }
        return Optional.ofNullable(investimentoService.listarPorCliente(clienteId))
                .filter(list -> !list.isEmpty())
                .map(Response::ok)
                .map(Response.ResponseBuilder::build)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Não encontrado nenhum investimento para o cliente ID " + clienteId)
                        .build());
    }
}
