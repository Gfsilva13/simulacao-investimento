package br.gov.caixa.entity.simulacao;

import br.gov.caixa.entity.business.Cliente;
import br.gov.caixa.entity.business.ProdutoInvestimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
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
    private @Min(value = 100, message = "O valor mínimo permitido é R$ 100") BigDecimal valorInvestido;

    @Column(nullable = false)
    private BigDecimal valorFinal;

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

    public @Min(value = 100, message = "O valor mínimo permitido é R$ 100") BigDecimal getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(@Min(value = 100, message = "O valor mínimo permitido é R$ 100") BigDecimal valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
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
