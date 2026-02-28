package dsm.api.pi.DTO.Barbeiro;

import dsm.api.pi.Enum.Unidade;
import lombok.Data;

@Data
public class BarbeiroUpdateDTO {
    private String nome;      // sem @NotBlank
    private String telefone;  // sem @NotBlank
    private Unidade unidade;
}
