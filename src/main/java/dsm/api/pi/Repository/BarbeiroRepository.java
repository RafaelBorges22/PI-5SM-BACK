package dsm.api.pi.Repository;

import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Enum.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    Optional<Barbeiro> findByNome(String nome);
    List<Barbeiro> findByUnidade(Unidade unidade);
}
