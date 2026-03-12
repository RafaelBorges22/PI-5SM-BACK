package dsm.api.pi.Service;

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

    public ServicoResponseDTO criarServico(ServiceAndPix request) {

        ServicoRequestDTO data = request.getData();
        PixRequestPayload pix = request.getPix();

        Cliente cliente = clienteRepository
                .findByNome(data.getNomeCliente())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente", data.getNomeCliente()));

        Barbeiro barbeiro = barbeiroRepository
                .findByNome(data.getNomeBarbeiro())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Barbeiro", data.getNomeBarbeiro()));

        Servico servico = new Servico();

        servico.setCliente(cliente);
        servico.setBarbeiro(barbeiro);
        servico.setValor(data.getValor());
        servico.setDataServico(data.getDataServico());
        servico.setMetodoPagamento(data.getMetodoPagamento());
        servico.setStatusPagamento(data.getStatusPagamento());
        servico.setProduto(data.getProduto());
        servico.setServico(data.getServico());

        // gerar pix se necessário
        if (data.getMetodoPagamento() == MetodoPagamento.PIX) {

            if (pix == null) {
                throw new IllegalArgumentException("Dados do PIX são obrigatórios");
            }

            gerarEInjetarPix(servico, data);
        }

        Servico salvo = servicoRepository.save(servico);

        return new ServicoResponseDTO(salvo);
    }
    private void gerarEInjetarPix(Servico servico, ServicoRequestDTO dto) {
        try {
            log.info(">>> Iniciando geração PIX para cliente: {}", dto.getNomeCliente());
            log.info(">>> Chave PIX config: {}", pixConfig.chavePix());
            log.info(">>> Valor: {}", dto.getValor());

            JSONObject cobranca = pixService.criarCobrancaServico(
                    dto.getNomeCliente(),
                    dto.getValor(),
                    pixConfig.chavePix()
            );

            log.info(">>> Resposta cobrança: {}", cobranca);

            if (cobranca == null) {
                log.warn("PIX não gerado: resposta nula da Efí");
                return;
            }

            // Verifica se a resposta contém erro
            if (cobranca.has("erro")) {
                log.error(">>> Erro retornado pela Efí: {}", cobranca.getString("erro"));
                return;
            }

            String txid = cobranca.getString("txid");
            String locId = cobranca.getJSONObject("loc").get("id").toString();

            log.info(">>> txid: {} | locId: {}", txid, locId);

            JSONObject qrData = pixService.gerarQrCodeImagem(locId);
            log.info(">>> QR Data: {}", qrData);

            if (qrData != null) {
                servico.setPixTxid(txid);
                servico.setPixQrCode(qrData.optString("qrcode"));
                servico.setPixImagemQrCode(qrData.optString("imagemQrcode"));
                servico.setPixLocId(locId);
                servico.setStatusPagamento(StatusPagamento.PENDENTE);
            }

        } catch (Exception e) {
            log.error(">>> ERRO COMPLETO ao gerar PIX: {}", e.getMessage(), e);
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
