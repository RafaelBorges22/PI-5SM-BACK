package dsm.api.pi.Service;

import dsm.api.pi.DTO.Cliente.ClienteRequestDTO;
import dsm.api.pi.DTO.Cliente.ClienteResponseDTO;
import dsm.api.pi.Entities.Cliente;
import dsm.api.pi.Repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .build();
        return new ClienteResponseDTO(clienteRepository.save(cliente));
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
