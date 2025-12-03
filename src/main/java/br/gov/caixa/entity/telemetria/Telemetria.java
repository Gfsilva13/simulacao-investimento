package br.gov.caixa.entity.telemetria;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Telemetria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String servico;
    public LocalDate periodoInicial;
    public LocalDate periodoFim;
    public long quantidadeChamadas;
    public double mediaTempoRespostaMs;

    }
