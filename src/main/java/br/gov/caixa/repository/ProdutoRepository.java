package br.gov.caixa.repository;

import br.gov.caixa.entity.ProdutoInvestimento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<ProdutoInvestimento> {

    public Optional<ProdutoInvestimento> findByTipo(String tipo){
                return find("tipoProduto = ?1",tipo).firstResultOptional();
    }

    public List<ProdutoInvestimento> findByPerfil(String perfilRisco){
        return switch (perfilRisco){
             case "Conservador" -> list("risco = ?1", "Baixo");
             case "Moderado" -> list("risco in (?1, ?2", "Baixo", "Médio");
             case "Agressivo" -> list("risco in (?1, ?2)", "Médio", "Alto");
            default -> List.of();
        };
    }

    public List<ProdutoInvestimento> listAllOrderedByRentabilidadeDesc(){
        return find("ORDER BY rentabilidade DESC").list();
    }

    public List<ProdutoInvestimento> findByRisco(String risco){
        return find("risco = ?1", risco).list();
    }
}
