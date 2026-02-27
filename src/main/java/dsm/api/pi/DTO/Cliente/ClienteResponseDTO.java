package dsm.api.pi.DTO.Cliente;

import dsm.api.pi.Entities.Cliente;
import lombok.Data;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String telefone;

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
    }
}