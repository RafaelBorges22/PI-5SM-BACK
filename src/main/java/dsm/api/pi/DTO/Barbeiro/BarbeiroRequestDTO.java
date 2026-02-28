package dsm.api.pi.DTO.Barbeiro;

import dsm.api.pi.Enum.Unidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BarbeiroRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @NotNull(message = "Unidade obrigatória")
    private Unidade unidade;
}
