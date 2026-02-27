package dsm.api.pi.Service;

import dsm.api.pi.DTO.Servico.ServicoRequestDTO;
import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Entities.Cliente;
import dsm.api.pi.Entities.Servico;
import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Repository.BarbeiroRepository;
import dsm.api.pi.Repository.ClienteRepository;
import dsm.api.pi.Repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;


    public ServicoResponseDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + id));
        return new ServicoResponseDTO(servico);
    }

    //IMPLEMENTAR QUANDO PUDER
    public List<ServicoResponseDTO> buscarPorBarbeiro(Long barbeiroId) {
        return servicoRepository.findByBarbeiroId(barbeiroId)
                .stream()
                .map(ServicoResponseDTO::new)
                .collect(Collectors.toList());
    }
//ESSE TBM
    public List<ServicoResponseDTO> buscarPorStatus(StatusPagamento status) {
        return servicoRepository.findByStatusPagamento(status)
                .stream()
                .map(ServicoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ServicoResponseDTO criar(ServicoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + dto.getClienteId()));

        Barbeiro barbeiro = barbeiroRepository.findById(dto.getBarbeiroId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado com id: " + dto.getBarbeiroId()));

        Servico servico = Servico.builder()
                .valor(dto.getValor())
                .dataServico(dto.getDataServico())
                .cliente(cliente)
                .barbeiro(barbeiro)
                .statusPagamento(dto.getStatusPagamento())
                .metodoPagamento(dto.getMetodoPagamento())
                .produto(dto.getProduto())
                .unidade(dto.getUnidade())
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
