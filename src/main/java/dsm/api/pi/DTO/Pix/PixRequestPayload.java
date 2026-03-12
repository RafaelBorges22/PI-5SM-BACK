package dsm.api.pi.DTO.Pix;

import java.math.BigDecimal;

public record PixRequestPayload(String chave, BigDecimal valor) {
}