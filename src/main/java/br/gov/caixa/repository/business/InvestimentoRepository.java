package br.gov.caixa.repository.business;

import br.gov.caixa.entity.business.Investimento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class InvestimentoRepository implements PanacheRepository<Investimento> {

    public List<Investimento> findByCliente(Long clienteId){
         return list("cliente.id", clienteId);
    }

    public long countByCliente(Long clienteId){
        return count("cliente.id", clienteId);
    }

    public double totalInvestidoPorCliente(Long clienteId){
        return find("cliente.id", clienteId).stream().
                mapToDouble(i -> i.getValor()).sum();
    }
}
