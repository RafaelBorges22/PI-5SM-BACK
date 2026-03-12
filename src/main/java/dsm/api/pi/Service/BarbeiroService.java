package dsm.api.pi.Service;

import dsm.api.pi.DTO.Barbeiro.BarbeiroRequestDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroResponseDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroUpdateDTO;
import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Exception.ResourceNotFoundException;
import dsm.api.pi.Repository.BarbeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    public BarbeiroResponseDTO buscarPorId(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", id));
        return new BarbeiroResponseDTO(barbeiro);
    }

    public List<BarbeiroResponseDTO> listarTodos() {
        return barbeiroRepository.findAll()
                .stream()
                .map(BarbeiroResponseDTO::new)
                .toList();
    }

    public BarbeiroResponseDTO criar(BarbeiroRequestDTO dto) {
        Barbeiro barbeiro = Barbeiro.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .unidade(dto.getUnidade())
                .build();
        return new BarbeiroResponseDTO(barbeiroRepository.save(barbeiro));
    }

    public BarbeiroResponseDTO atualizar(Long id, BarbeiroUpdateDTO dto) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", id));

        if (dto.getNome() != null) barbeiro.setNome(dto.getNome());
        if (dto.getTelefone() != null) barbeiro.setTelefone(dto.getTelefone());
        if (dto.getUnidade() != null) barbeiro.setUnidade(dto.getUnidade());

        return new BarbeiroResponseDTO(barbeiroRepository.save(barbeiro));
    }

    public void deletar(Long id) {
        if (!barbeiroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Barbeiro", id);
        }
        barbeiroRepository.deleteById(id);
    }
}