package dsm.api.pi.Repository;

import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Entities.Cliente;
import dsm.api.pi.Entities.Servico;
import dsm.api.pi.Enum.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    // Buscar serviços de hoje
    List<Servico> findByDataServico(LocalDate data);

    // Buscar por cliente
    List<Servico> findByClienteId(Long clienteId);

    // Buscar por barbeiro
    List<Servico> findByBarbeiroId(Long barbeiroId);

    // Buscar por status de pagamento
    List<Servico> findByStatusPagamento(StatusPagamento statusPagamento);

    // Buscar por período
    List<Servico> findByDataServicoBetween(LocalDate inicio, LocalDate fim);

    // Buscar por barbeiro e data
    List<Servico> findByBarbeiroIdAndDataServico(Long barbeiroId, LocalDate data);

}
