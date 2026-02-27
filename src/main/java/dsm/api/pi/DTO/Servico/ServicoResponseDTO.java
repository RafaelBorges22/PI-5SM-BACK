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
    private Long clienteId;
    private String nomeCliente;
    private Long barbeiroId;
    private String nomeBarbeiro;
    private StatusPagamento statusPagamento;
    private MetodoPagamento metodoPagamento;
    private String produto;
    private Unidade unidade;

    public ServicoResponseDTO(Servico servico) {
        this.id = servico.getId();
        this.valor = servico.getValor();
        this.dataServico = servico.getDataServico();
        this.clienteId = servico.getCliente().getId();
        this.nomeCliente = servico.getCliente().getNome();
        this.barbeiroId = servico.getBarbeiro().getId();
        this.nomeBarbeiro = servico.getBarbeiro().getNome();
        this.statusPagamento = servico.getStatusPagamento();
        this.metodoPagamento = servico.getMetodoPagamento();
        this.produto = servico.getProduto();
        this.unidade = servico.getUnidade();
    }
}

