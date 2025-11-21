package br.gov.caixa.dto;

import jakarta.validation.constraints.NotNull;

public class PerfilRiscoDTO {

    @NotNull public Long clienteId;
    @NotNull public String perfil;
    @NotNull public int pontuacao;
    @NotNull public String descricao;

    public PerfilRiscoDTO(){

    }
    public PerfilRiscoDTO(Long clienteId, String perfil, int pontuacao, String descricao){
        this.clienteId = clienteId;
        this.perfil = perfil;
        this.pontuacao = pontuacao;
        this.descricao = descricao;
    }
}
