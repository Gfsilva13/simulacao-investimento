package br.gov.caixa.repository;

import br.gov.caixa.entity.ParametroProduto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametroProdutoRepository implements PanacheRepository<ParametroProduto> {

    public ParametroProduto findByTipo(String tipo){
       return find("tipoProduto = ?1", tipo).firstResult();
    }

    public ParametroProduto findByTipoAndRisco(String tipoProduto, String riscoAceito) {
        return find("tipoProduto =?1", "risco= ?2", tipoProduto, riscoAceito).firstResult();
    }
}
