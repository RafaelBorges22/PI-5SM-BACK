package dsm.api.pi.Controller;


import dsm.api.pi.DTO.Barbeiro.BarbeiroResponseDTO;
import dsm.api.pi.Service.BarbeiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/barbeiro")
public class BarbeiroController {

    @Autowired
    private BarbeiroService barbeiroService;

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(barbeiroService.listarTodos());
    }
}
