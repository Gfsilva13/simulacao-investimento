package br.gov.caixa.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/test-validation")
public class ValidationTestResource {

    @GET
    public String test(
            @QueryParam("nome")
            @NotBlank(message = "Nome é obrigatório") String nome) {
        return "OK";
    }
}

