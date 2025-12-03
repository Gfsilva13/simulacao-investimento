package br.gov.caixa.entity.business;

import jakarta.persistence.*;

@Entity
@Table(name = "parametro_produto")
public class ParametroProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double minValor;

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

   public Double getMinValor() {
        return minValor;
    }

    public void setMinValor(Double minValor) {
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
