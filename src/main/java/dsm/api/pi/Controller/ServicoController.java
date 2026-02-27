package dsm.api.pi.Controller;

import dsm.api.pi.DTO.Servico.ServicoRequestDTO;
import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }

    @GetMapping("/hoje")
    public ResponseEntity<Object> buscarServicosHoje() {
        return ResponseEntity.ok(servicoService.buscarServicosHoje());
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody ServicoRequestDTO dto) {
        return ResponseEntity.status(201).body(servicoService.criar(dto));
    }
}
