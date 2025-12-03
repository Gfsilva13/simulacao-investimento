package br.gov.caixa.repository.business;

import br.gov.caixa.entity.business.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public Optional<Cliente> findByOptional(Long id){
        return find("id", id).firstResultOptional();
    }
}
