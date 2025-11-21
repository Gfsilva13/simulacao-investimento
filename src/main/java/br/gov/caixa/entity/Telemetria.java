package br.gov.caixa.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Telemetria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String servico;

    @Column(nullable = false)
    private Integer qtdChamadas;

    @Column(nullable = false)
    private Integer tempoMedioMs;

    @Column(nullable = false)
    private LocalDate periodoInicial;

    @Column(nullable = false)
    private LocalDate periodoFim;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public Integer getQtdChamadas() {
        return qtdChamadas;
    }

    public void setQtdChamadas(Integer qtdChamadas) {
        this.qtdChamadas = qtdChamadas;
    }

    public Integer getTempoMedioMs() {
        return tempoMedioMs;
    }

    public void setTempoMedioMs(Integer tempoMedioMs) {
        this.tempoMedioMs = tempoMedioMs;
    }

    public LocalDate getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(LocalDate periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }
}
