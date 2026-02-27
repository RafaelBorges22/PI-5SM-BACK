package dsm.api.pi.Repository;

import dsm.api.pi.Entities.Servico;
import dsm.api.pi.Enum.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByClienteId(Long clienteId);

    List<Servico> findByBarbeiroId(Long barbeiroId);

    List<Servico> findByStatusPagamento(StatusPagamento status);

    List<Servico> findByDataServicoBetween(LocalDate inicio, LocalDate fim);

    List<Servico> findByDataServico(LocalDate data);
}
