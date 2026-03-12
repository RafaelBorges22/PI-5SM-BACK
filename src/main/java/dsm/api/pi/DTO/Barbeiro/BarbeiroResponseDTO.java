package dsm.api.pi.DTO.Barbeiro;

import dsm.api.pi.Entities.Barbeiro;
import dsm.api.pi.Enum.Unidade;
import lombok.Data;

@Data
public class BarbeiroResponseDTO {

    private Long id;
    private String nome;
    private String telefone;
    private Unidade unidade;

    public BarbeiroResponseDTO(Barbeiro barbeiro) {
        this.id = barbeiro.getId();
        this.nome = barbeiro.getNome();
        this.telefone = barbeiro.getTelefone();
        this.unidade = barbeiro.getUnidade();
    }
}