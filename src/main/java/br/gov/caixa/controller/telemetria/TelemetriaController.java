package br.gov.caixa.controller.telemetria;

import br.gov.caixa.telemetria.TelemetriaFiltroRequest;
import br.gov.caixa.dto.telemetria.TelemetriaDTO;
import br.gov.caixa.service.telemetria.TelemetriaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/telemetria")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Telemetria")

public class TelemetriaController {

    @Inject
    TelemetriaService telemetriaService;

    @GET
    @RolesAllowed({"admin"})
    public Response obterResumo(@Valid @BeanParam TelemetriaFiltroRequest filtro){
        TelemetriaDTO resultado = telemetriaService.gerarResumo(filtro.dias);
        return Response.ok(resultado).build();
    }
}
