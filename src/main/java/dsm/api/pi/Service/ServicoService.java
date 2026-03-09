package dsm.api.pi.Service;

import dsm.api.pi.Config.PixConfig;
import dsm.api.pi.DTO.Servico.ServicoRequestDTO;
import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Entities.Cliente;
import dsm.api.pi.Entities.Servico;
import dsm.api.pi.Enum.MetodoPagamento;
import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Exception.ResourceNotFoundException;
import dsm.api.pi.Repository.BarbeiroRepository;
import dsm.api.pi.Repository.ClienteRepository;
import dsm.api.pi.Repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final PixService pixService;
    private final PixConfig pixConfig;

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

        if (dto.getMetodoPagamento() == MetodoPagamento.PIX) {
            gerarEInjetarPix(servico, dto);
        }

        return new ServicoResponseDTO(servicoRepository.save(servico));
    }
    private void gerarEInjetarPix(Servico servico, ServicoRequestDTO dto) {
        try {
            // Passo 1 — cria cobrança e obtém txid + locId
            JSONObject cobranca = pixService.criarCobrancaServico(
                    dto.getNomeCliente(),
                    dto.getValor(),
                    pixConfig.chavePix()       // chave vem do config
            );

            if (cobranca == null) {
                log.warn("PIX não gerado: resposta nula da Efí");
                return;
            }

            String txid = cobranca.getString("txid");
            String locId = String.valueOf(cobranca.getJSONObject("loc").getInt("id"));

            // Passo 2 — gera imagem QR Code com o locId
            JSONObject qrData = pixService.gerarQrCodeImagem(locId);

            if (qrData != null) {
                servico.setPixTxid(txid);
                servico.setPixQrCode(qrData.optString("qrcode"));
                servico.setPixImagemQrCode(qrData.optString("imagemQrcode")); // base64
                servico.setPixLocId(locId);
                servico.setStatusPagamento(StatusPagamento.PENDENTE);
            }

        } catch (Exception e) {
            log.error("Erro ao gerar PIX para serviço: {}", e.getMessage());
            // não lança exceção — serviço é salvo mesmo sem PIX
        }
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
