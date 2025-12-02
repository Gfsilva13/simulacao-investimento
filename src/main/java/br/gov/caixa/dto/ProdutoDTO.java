package br.gov.caixa.dto;

import br.gov.caixa.entity.ProdutoInvestimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProdutoDTO {

    @NotNull
    public Long id;

    @NotBlank
    public String nome;

    @NotBlank
    public String tipo;

    @NotNull
    public Double rentabilidade;

    @NotBlank
    public String risco;

    public static ProdutoDTO fromEntity(ProdutoInvestimento produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.id = produto.getId();
        dto.nome = produto.getNomeProduto();
        dto.tipo = produto.getTipoProduto();
        dto.rentabilidade = produto.getRentabilidade();
        dto.risco = produto.getParametroProduto().getRiscoAceito();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getRentabilidade() {
        return rentabilidade;
    }

    public void setRentabilidade(Double rentabilidade) {
        this.rentabilidade = rentabilidade;
    }

    public String getRisco() {
        return risco;
    }

    public void setRisco(String risco) {
        this.risco = risco;
    }
}
