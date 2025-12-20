package br.gov.caixa.entity.business;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parametro_produto")
public class ParametroProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal minValor;

    @Column(nullable = false)
    private Integer maxPrazo;

    @Column(nullable = false)
    private String riscoAceito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   public BigDecimal getMinValor() {
        return minValor;
    }

    public void setMinValor(BigDecimal minValor) {
        this.minValor = minValor;
    }

    public Integer getMaxPrazo() {
        return maxPrazo;
    }

    public void setMaxPrazo(Integer maxPrazo) {
        this.maxPrazo = maxPrazo;
    }

    public String getRiscoAceito() {
        return riscoAceito;
    }

    public void setRiscoAceito(String riscoAceito) {
        this.riscoAceito = riscoAceito;
    }
}
