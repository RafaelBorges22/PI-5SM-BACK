package dsm.api.pi.Controller;


import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ServicoResponseDTO> criarServico(
            @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(servicoService.criarServico(body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}
