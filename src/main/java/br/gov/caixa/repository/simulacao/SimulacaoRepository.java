package br.gov.caixa.repository.simulacao;

import br.gov.caixa.entity.simulacao.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {

    public List<Simulacao> findByCliente(Long clienteId) {
        return list("cliente.id", clienteId);
    }

    public List<Simulacao> findAllOrdered() {
        return listAll(Sort.by("dataSimulacao").descending());
    }
}
