package dsm.api.pi.DTO.Servico;

import dsm.api.pi.Enum.MetodoPagamento;
import dsm.api.pi.Enum.StatusPagamento;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String nomeCliente;

    @NotBlank
    private String nomeBarbeiro;

    @NotNull
    private StatusPagamento statusPagamento;

    @NotNull
    private MetodoPagamento metodoPagamento;

    private String produto;

    private String servico;
}