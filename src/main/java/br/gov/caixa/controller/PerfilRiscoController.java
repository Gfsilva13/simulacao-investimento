package br.gov.caixa.controller;

import br.gov.caixa.dto.PerfilRiscoDTO;
import br.gov.caixa.service.PerfilRiscoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Path("/perfil-risco")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Perfil de Risco")
public class PerfilRiscoController {

    @Inject
    PerfilRiscoService perfilRiscoService;

    @GET
    @Path("/{clienteId}")
    @RolesAllowed({"admin","user"})
    public Response obterPerfil(@PathParam("clienteId") Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O ID do cliente deve ser um número positivo.")
                    .build();
        }
        try {
            return Optional.ofNullable(perfilRiscoService.calcularPerfil(clienteId))
                    .filter(list -> !list.isEmpty())
                    .map(Response::ok)
                    .map(Response.ResponseBuilder::build)
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                            .entity("Perfil de risco não encontrado para o cliente ID " + clienteId)
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao calcular perfil de risco: " + e.getMessage())
                    .build();
        }
    }
}
