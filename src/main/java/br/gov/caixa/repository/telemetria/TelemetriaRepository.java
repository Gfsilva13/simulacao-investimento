package br.gov.caixa.repository.telemetria;

import br.gov.caixa.entity.telemetria.Telemetria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class TelemetriaRepository implements PanacheRepository<Telemetria> {

    public List<Telemetria> findByPeriodo(LocalDate inicio, LocalDate fim){
        return find("periodoInicial >= ?1 and periodoFim <= ?2", inicio, fim).list();
    }
}


