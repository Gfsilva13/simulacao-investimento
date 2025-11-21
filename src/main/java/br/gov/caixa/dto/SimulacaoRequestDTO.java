package br.gov.caixa.dto;

import java.math.BigDecimal;

public class SimulacaoRequestDTO {

    public Long clienteId;
    public Long produtoId;
    public BigDecimal valorInicial;
    public int prazoMeses;
}
