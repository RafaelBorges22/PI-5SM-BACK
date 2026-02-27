package dsm.api.pi.DTO.Servico;

import dsm.api.pi.Enum.MetodoPagamento;
import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Enum.Unidade;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ServicoRequestDTO {

    @NotNull
    private BigDecimal valor;

    @NotNull
    private LocalDate dataServico;

    @NotNull
    private Long clienteId;

    @NotNull
    private Long barbeiroId;

    @NotNull
    private StatusPagamento statusPagamento;

    private MetodoPagamento metodoPagamento;

    private String produto;

    private Unidade unidade;
}