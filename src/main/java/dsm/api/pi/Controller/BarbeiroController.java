package dsm.api.pi.Controller;


import dsm.api.pi.DTO.Barbeiro.BarbeiroRequestDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroResponseDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroUpdateDTO;
import dsm.api.pi.Service.BarbeiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbeiro")
@RequiredArgsConstructor
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(barbeiroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(barbeiroService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> criar(@Valid @RequestBody BarbeiroRequestDTO dto) {
        return ResponseEntity.status(201).body(barbeiroService.criar(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> atualizar(@PathVariable Long id,
                                                         @RequestBody BarbeiroUpdateDTO dto) {
        return ResponseEntity.ok(barbeiroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
