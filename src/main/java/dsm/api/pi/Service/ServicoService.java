package dsm.api.pi.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsm.api.pi.Config.PixConfig;
import dsm.api.pi.DTO.Pix.PixRequestPayload;
import dsm.api.pi.DTO.Servico.ServiceAndPix;
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
    private final BarbeiroRepository barbeiroRepository;
    private final PixService pixService;
    private final PixConfig pixConfig;
    private final ObjectMapper objectMapper;


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

    public ServicoResponseDTO criarServico(Map<String, Object> body) {

        // Detecta se é PIX (body tem campo "data") ou pagamento simples
        boolean isPix = body.containsKey("data");

        if (isPix) {
            return criarServicoComPix(body);
        } else {
            return criarServicoSimples(body);
        }
    }

    // Cartão / Dinheiro — body direto
    private ServicoResponseDTO criarServicoSimples(Map<String, Object> body) {
        ServicoRequestDTO dto = objectMapper.convertValue(body, ServicoRequestDTO.class);

        Barbeiro barbeiro = barbeiroRepository
                .findByNome(dto.getNomeBarbeiro())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", dto.getNomeBarbeiro()));

        Servico servico = new Servico();
        servico.setNomeCliente(dto.getNomeCliente());
        servico.setBarbeiro(barbeiro);
        servico.setValor(dto.getValor());
        servico.setDataServico(dto.getDataServico());
        servico.setMetodoPagamento(dto.getMetodoPagamento());
        servico.setStatusPagamento(dto.getStatusPagamento());
        servico.setProduto(dto.getProduto());
        servico.setServico(dto.getServico());

        return new ServicoResponseDTO(servicoRepository.save(servico));
    }

    // PIX — body com "data" e "pix"
    private ServicoResponseDTO criarServicoComPix(Map<String, Object> body) {
        Map<String, Object> dataMap = (Map<String, Object>) body.get("data");
        ServicoRequestDTO dto = objectMapper.convertValue(dataMap, ServicoRequestDTO.class);

        Barbeiro barbeiro = barbeiroRepository
                .findByNome(dto.getNomeBarbeiro())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", dto.getNomeBarbeiro()));

        Servico servico = new Servico();
        servico.setNomeCliente(dto.getNomeCliente());
        servico.setBarbeiro(barbeiro);
        servico.setValor(dto.getValor());
        servico.setDataServico(dto.getDataServico());
        servico.setMetodoPagamento(MetodoPagamento.PIX);
        servico.setStatusPagamento(StatusPagamento.PENDENTE);
        servico.setProduto(dto.getProduto());
        servico.setServico(dto.getServico());

        gerarEInjetarPix(servico, dto);

        return new ServicoResponseDTO(servicoRepository.save(servico));
    }

    private void gerarEInjetarPix(Servico servico, ServicoRequestDTO dto) {
        try {
            log.info(">>> Iniciando geração PIX para cliente: {}", dto.getNomeCliente());

            JSONObject cobranca = pixService.criarCobrancaServico(
                    dto.getNomeCliente(),
                    dto.getValor(),
                    pixConfig.chavePix()
            );

            if (cobranca == null || cobranca.has("erro")) {
                log.warn("PIX não gerado: {}", cobranca);
                return;
            }

            String txid = cobranca.getString("txid");
            String locId = cobranca.getJSONObject("loc").get("id").toString();

            JSONObject qrData = pixService.gerarQrCodeImagem(locId);

            if (qrData != null) {
                servico.setPixTxid(txid);
                servico.setPixQrCode(qrData.optString("qrcode"));
                servico.setPixImagemQrCode(qrData.optString("imagemQrcode"));
                servico.setPixLocId(locId);
                servico.setStatusPagamento(StatusPagamento.PENDENTE);
            }

        } catch (Exception e) {
            log.error(">>> ERRO ao gerar PIX: {}", e.getMessage(), e);
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

    public void deletarServico(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new RuntimeException("Serviço não encontrado com id: " + id);
        }
        servicoRepository.deleteById(id);
    }
}
