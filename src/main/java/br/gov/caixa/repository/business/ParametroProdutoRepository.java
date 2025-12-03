package br.gov.caixa.repository.business;

import br.gov.caixa.entity.business.ParametroProduto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametroProdutoRepository implements PanacheRepository<ParametroProduto> {

    public ParametroProduto findByTipo(String tipo){
       return find("tipoProduto = ?1", tipo).firstResult();
    }
}
