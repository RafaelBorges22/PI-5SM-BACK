package dsm.api.pi.DTO.Servico;

import dsm.api.pi.DTO.Pix.PixRequestPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceAndPix {

    @Valid
    @NotNull
    private ServicoRequestDTO data;

    @Valid
    private PixRequestPayload pix;
}