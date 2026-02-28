package dsm.api.pi.Service;

import dsm.api.pi.DTO.Servico.ServicoRequestDTO;
import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Entities.Cliente;
import dsm.api.pi.Entities.Servico;
import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Exception.ResourceNotFoundException;
import dsm.api.pi.Repository.BarbeiroRepository;
import dsm.api.pi.Repository.ClienteRepository;
import dsm.api.pi.Repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;

    public List<ServicoResponseDTO> listarTodos() {
        return servicoRepository.findAll()
                .stream()
                .map(ServicoResponseDTO::new)
                .toList();
    }

    public ServicoResponseDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + id));
        return new ServicoResponseDTO(servico);
    }

    public ServicoResponseDTO criar(ServicoRequestDTO dto) {
        Cliente cliente = clienteRepository.findByNome(dto.getNomeCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", dto.getNomeCliente()));

        Barbeiro barbeiro = barbeiroRepository.findByNome(dto.getNomeBarbeiro())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", dto.getNomeBarbeiro()));

        Servico servico = Servico.builder()
                .valor(dto.getValor())
                .dataServico(dto.getDataServico())
                .cliente(cliente)
                .barbeiro(barbeiro)
                .statusPagamento(dto.getStatusPagamento())
                .metodoPagamento(dto.getMetodoPagamento())
                .produto(dto.getProduto())
                .servico(dto.getServico())
                .build();

        return new ServicoResponseDTO(servicoRepository.save(servico));
    }

    public Object buscarServicosHoje() {
        LocalDate hoje = LocalDate.now();
        List<Servico> servicos = servicoRepository.findByDataServico(hoje);

        if (servicos.isEmpty()) {
            return Map.of("message", "Sem serviços registrados hoje");
        }

        return servicos.stream()
                .map(ServicoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
