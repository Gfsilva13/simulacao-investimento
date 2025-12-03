package br.gov.caixa.service.telemetria;

import br.gov.caixa.dto.telemetria.PeriodoDTO;
import br.gov.caixa.dto.telemetria.ServicoTelemetriaDTO;
import br.gov.caixa.dto.telemetria.TelemetriaDTO;

import br.gov.caixa.entity.telemetria.Telemetria;
import br.gov.caixa.repository.telemetria.TelemetriaRepository;
import br.gov.caixa.telemetria.TelemetriaMetrics;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TelemetriaService {

    @Inject
    TelemetriaMetrics telemetriaMetrics;
    @Inject
    TelemetriaRepository telemetriaRepository;

    @Transactional
    public TelemetriaDTO gerarResumo(int dias) {

        ZoneId zone = ZoneId.of("America/Sao_Paulo");
        LocalDate fim = LocalDate.now(zone);
        LocalDate inicio = fim.minusDays(dias);

        Map<String, String> servicosMonitorados = Map.of(
                "perfilRisco", "perfil-risco",
                "simularInvestimento", "simular-investimento"
        );
        List<ServicoTelemetriaDTO> servicos = new ArrayList<>();
        for (Map.Entry<String, String> entry : servicosMonitorados.entrySet()) {
            String metodo = entry.getKey();
            String servico = entry.getValue();
            long chamadas = (long) telemetriaMetrics.getChamadas(metodo);
            long media = (long) telemetriaMetrics.getTempoMedio(metodo);
            servicos.add(
                    new ServicoTelemetriaDTO(servico, chamadas, media)
            );
            Telemetria t = new Telemetria();
            t.servico = servico;
            t.periodoInicial = inicio;
            t.periodoFim = fim;
            t.quantidadeChamadas = chamadas;
            t.mediaTempoRespostaMs = media;
            telemetriaRepository.persist(t);
        }
        PeriodoDTO periodo = new PeriodoDTO(inicio.toString(), fim.toString());
        return new TelemetriaDTO(servicos, periodo);
    }
}
