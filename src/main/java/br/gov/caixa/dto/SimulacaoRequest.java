package br.gov.caixa.dto;

import jakarta.validation.constraints.*;

public class SimulacaoRequest {

    @NotNull(message = "O ID do cliente é obrigatório")
    public Long clienteId;

    @NotBlank(message = "O tipo do produto é obrigatório")
    public String tipoProduto;

    @Min(value = 100, message = "O valor mínimo permitido é R$ 100")
    public double valor;

    @Min(value = 1, message = "O prazo mínimo é 1 mês")
    @Max(value = 360, message = "O prazo máximo é 360 meses")
    public int prazoMeses;

}
