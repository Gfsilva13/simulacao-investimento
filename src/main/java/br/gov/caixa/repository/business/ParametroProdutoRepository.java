package br.gov.caixa.repository.business;

import br.gov.caixa.entity.business.ParametroProduto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametroProdutoRepository implements PanacheRepository<ParametroProduto> {


    public ParametroProduto findByRisco(String risco){
       return find("riscoAceito = ?1", risco).firstResult();
    }
}
