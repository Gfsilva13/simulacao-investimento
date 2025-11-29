package br.gov.caixa.repository;

import br.gov.caixa.entity.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public Optional<Cliente> findByOptional(Long id){
        return find("id", id).firstResultOptional();
    }
}
