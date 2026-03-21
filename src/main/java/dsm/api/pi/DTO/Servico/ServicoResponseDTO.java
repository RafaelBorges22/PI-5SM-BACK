package dsm.api.pi.DTO.Servico;

import dsm.api.pi.Entities.Servico;
import dsm.api.pi.Enum.MetodoPagamento;
import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Enum.Unidade;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ServicoResponseDTO {

    private Long id;
    private BigDecimal valor;
    private LocalDate dataServico;
    private String nomeCliente;
    private String nomeBarbeiro;
    private StatusPagamento statusPagamento;
    private MetodoPagamento metodoPagamento;
    private String produto;
    private String servico;
    private Unidade unidade;
    private String pixTxid;
    private String pixQrCode;
    private String pixImagemQrCode;

    public ServicoResponseDTO(Servico servico) {
        this.id = servico.getId();
        this.valor = servico.getValor();
        this.dataServico = servico.getDataServico();
        this.nomeCliente = servico.getNomeCliente();
        this.nomeBarbeiro = servico.getBarbeiro().getNome();
        this.statusPagamento = servico.getStatusPagamento();
        this.metodoPagamento = servico.getMetodoPagamento();
        this.produto = servico.getProduto();
        this.unidade = servico.getBarbeiro().getUnidade();
        this.servico = servico.getServico();
        this.pixTxid = servico.getPixTxid();
        this.pixQrCode = servico.getPixQrCode();
        this.pixImagemQrCode = servico.getPixImagemQrCode();
    }
}

