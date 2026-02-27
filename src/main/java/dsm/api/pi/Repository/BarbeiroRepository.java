package dsm.api.pi.Repository;

import dsm.api.pi.Entities.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    List<Barbeiro> findByNomeContainingIgnoreCase(String nome);
}
