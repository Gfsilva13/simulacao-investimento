package br.gov.caixa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "produto_investimento")
public class ProdutoInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false)
    private String tipoProduto;

    @Column(nullable = false)
    private Double rentabilidade;

    @ManyToOne
    @JoinColumn(name ="parametroId", nullable = false)
    private ParametroProduto parametroProduto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public Double getRentabilidade() {
        return rentabilidade;
    }

    public void setRentabilidade(Double rentabilidade) {
        this.rentabilidade = rentabilidade;
    }

    public ParametroProduto getParametroProduto() {
        return parametroProduto;
    }

    public void setParametroProduto(ParametroProduto parametroProduto) {
        this.parametroProduto = parametroProduto;
    }
}
