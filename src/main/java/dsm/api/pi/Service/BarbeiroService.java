package dsm.api.pi.Service;

import dsm.api.pi.DTO.Barbeiro.BarbeiroRequestDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroResponseDTO;
import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Repository.BarbeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    public List<BarbeiroResponseDTO> listarTodos() {
        return barbeiroRepository.findAll()
                .stream()
                .map(BarbeiroResponseDTO::new)
                .collect(Collectors.toList());
    }

    public BarbeiroResponseDTO criar(BarbeiroRequestDTO dto) {
        Barbeiro barbeiro = Barbeiro.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .build();
        return new BarbeiroResponseDTO(barbeiroRepository.save(barbeiro));
    }

    public void deletar(Long id) {
        if (!barbeiroRepository.existsById(id)) {
            throw new RuntimeException("Barbeiro não encontrado com id: " + id);
        }
        barbeiroRepository.deleteById(id);
    }
}
