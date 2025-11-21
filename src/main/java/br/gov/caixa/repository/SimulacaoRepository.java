package br.gov.caixa.repository;

import br.gov.caixa.entity.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {

    public List<Simulacao> findByCliente(Long clienteId) {
        return list("clienteId", clienteId);
    }

    public List<Simulacao> findByProdutoAndDate(String produtoNome, LocalDate data) {
        return find("produto.nome = ?1 and date(dataSimulacao) = ?2", produtoNome, data).list();
    }

    public List<Simulacao> findAllOrdered() {
        return listAll(Sort.by("dataSimulacao").descending());
    }
}
