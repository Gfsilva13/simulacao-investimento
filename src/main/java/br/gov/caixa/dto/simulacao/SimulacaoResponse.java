package br.gov.caixa.dto.simulacao;

import br.gov.caixa.dto.business.ProdutoDTO;

import java.time.LocalDateTime;

public class SimulacaoResponse {

    public ProdutoDTO produtoValidado;
    public ResultadoSimulacaoDTO resultadoSimulacao;
    public LocalDateTime dataSimulacao;

}
