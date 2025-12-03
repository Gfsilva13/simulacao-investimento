package br.gov.caixa.telemetria;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TelemetriaMetrics {

    @Inject
    MeterRegistry registry;

    private final Map<String, Counter> contadorChamadas = new HashMap<>();
    private final Map<String, Timer> tempoResposta = new HashMap<>();

    public void registrarChamada(String servico, long tempoExecucaoMs) {

        contadorChamadas.computeIfAbsent(servico, s ->
                Counter.builder("telemetria_chamadas_total")
                        .tag("servico", s)
                        .description("Quantidade de chamadas por serviço")
                        .register(registry)
        ).increment();

        tempoResposta.computeIfAbsent(servico, s ->
                Timer.builder("telemetria_tempo_resposta_ms")
                        .tag("servico", s)
                        .description("Tempo de resposta do serviço")
                        .register(registry)
        ).record(tempoExecucaoMs, TimeUnit.MILLISECONDS);
    }

    public double getTempoMedio(String servico) {
        return tempoResposta.get(servico) != null ? tempoResposta.get(servico)
                .mean(TimeUnit.MILLISECONDS) : 0;
    }

    public double getChamadas(String servico) {
        return contadorChamadas.get(servico) != null ? contadorChamadas.get(servico).count() : 0;
    }
}
