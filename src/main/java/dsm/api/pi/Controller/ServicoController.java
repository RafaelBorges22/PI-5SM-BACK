package dsm.api.pi.Controller;

import dsm.api.pi.DTO.Servico.ServiceAndPix;
import dsm.api.pi.DTO.Servico.ServicoRequestDTO;
import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }

    @GetMapping("/hoje")
    public ResponseEntity<Object> buscarServicosHoje() {
        return ResponseEntity.ok(servicoService.buscarServicosHoje());
    }

    @PostMapping
    public ResponseEntity<?> criarServico(@Valid @RequestBody ServiceAndPix request) {

        ServicoResponseDTO response = servicoService.criarServico(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}
