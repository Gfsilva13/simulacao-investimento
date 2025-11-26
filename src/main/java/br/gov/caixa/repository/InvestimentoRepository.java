package br.gov.caixa.repository;

import br.gov.caixa.entity.Investimento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvestimentoRepository implements PanacheRepository<Investimento> {

    public List<Investimento> findByCliente(Long clienteId){

        return list("clienteId", clienteId);
    }

    public long countByCliente(Long clienteId){
        return count("clienteId", clienteId);
    }

    public double totalInvestidoPorCliente(Long clienteId){
        return find("clienteId", clienteId).stream().
                mapToDouble(i -> i.getValor()).sum();
    }
}
