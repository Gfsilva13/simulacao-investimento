package br.gov.caixa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteId", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtoId", nullable = false)
    private ProdutoInvestimento produto;

    @Column(nullable = false)
    private Double valorInvestido;

    @Column(nullable = false)
    private Double valorFinal;

    @Column(nullable = false)
    private Integer prazoMeses;

    @Column(nullable = false)
    private LocalDateTime dataSimulacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(Double valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public LocalDateTime getDataSimulacao() {
        return dataSimulacao;
    }

    public void setDataSimulacao(LocalDateTime dataSimulacao) {
        this.dataSimulacao = dataSimulacao;
    }

    public ProdutoInvestimento getProduto() {
        return produto;
    }

    public void setProduto(ProdutoInvestimento produto) {
        this.produto = produto;
    }
}
