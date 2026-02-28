package dsm.api.pi.Service;

import dsm.api.pi.DTO.Barbeiro.BarbeiroResponseDTO;
import dsm.api.pi.DTO.Cliente.ClienteRequestDTO;
import dsm.api.pi.DTO.Cliente.ClienteResponseDTO;
import dsm.api.pi.DTO.Cliente.ClienteUpdateDTO;
import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Entities.Cliente;
import dsm.api.pi.Exception.ResourceNotFoundException;
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
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
        return new ClienteResponseDTO(cliente);
    }

    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .build();
        return new ClienteResponseDTO(clienteRepository.save(cliente));
    }

    public ClienteResponseDTO atualizar(Long id, ClienteUpdateDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));

        if (dto.getNome() != null) cliente.setNome(dto.getNome());
        if (dto.getTelefone() != null) cliente.setTelefone(dto.getTelefone());

        return new ClienteResponseDTO(clienteRepository.save(cliente));
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }
}