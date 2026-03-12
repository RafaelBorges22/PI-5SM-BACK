package dsm.api.pi.Controller;

import dsm.api.pi.DTO.Pix.PixRequestPayload;
import dsm.api.pi.Service.PixService;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/pix", produces = MediaType.APPLICATION_JSON_VALUE)
public record PixController(PixService pixService) {


    @GetMapping("/listar")
    public ResponseEntity<String> listarChavesPix(){
        var response = this.pixService.listarChavesPix();
        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping
    public ResponseEntity<String> criarChavePix(){
        var response = this.pixService.criarChavePix();
        return ResponseEntity.ok().body(response.toString());
    }

    @DeleteMapping
    public ResponseEntity<String> deletarChavePix(@RequestParam("chavePix") String chavePix) {
        var response = this.pixService.deletarChavePix(chavePix);
        return ResponseEntity.ok()
                .body(response.toString());
    }

    @PostMapping
    public ResponseEntity<String> criarQrCode(@RequestBody PixRequestPayload pixRequestPayload) {
        var response = this.pixService.criarQrCode(pixRequestPayload);

        if (response == null) {
            return ResponseEntity.internalServerError()
                    .body("{\"erro\": \"Falha ao gerar QR Code PIX\"}");
        }

        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping("/qrcode")
    public ResponseEntity<String> gerarQrCode(@RequestParam("id") String locId) {
        try {
            JSONObject response = this.pixService.gerarQrCodeImagem(locId);
            if (response == null) {
                return ResponseEntity.internalServerError()
                        .body("{\"erro\": \"Falha ao gerar QR Code\"}");
            }
            return ResponseEntity.ok().body(response.toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

}