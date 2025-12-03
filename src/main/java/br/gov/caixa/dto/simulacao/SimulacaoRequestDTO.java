package br.gov.caixa.dto.simulacao;

import java.math.BigDecimal;

public class SimulacaoRequestDTO {

    public Long clienteId;
    public Long produtoId;
    public BigDecimal valorInicial;
    public int prazoMeses;
}
