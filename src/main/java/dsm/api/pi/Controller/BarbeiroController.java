package dsm.api.pi.Controller;

import dsm.api.pi.DTO.Barbeiro.BarbeiroRequestDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroResponseDTO;
import dsm.api.pi.DTO.Barbeiro.BarbeiroUpdateDTO;
import dsm.api.pi.Service.BarbeiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbeiro")
@RequiredArgsConstructor
@Tag(name = "Barbeiros", description = "Gerenciamento de barbeiros")
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    @Operation(summary = "Listar todos os barbeiros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 500,
                    "error": "Erro Interno",
                    "message": "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                    "path": "/api/barbeiro",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """)))
    })
    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(barbeiroService.listarTodos());
    }

    @Operation(summary = "Buscar barbeiro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 404,
                    "error": "Não Encontrado",
                    "message": "Barbeiro não encontrado(a) com id: 99",
                    "path": "/api/barbeiro/99",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(barbeiroService.buscarPorId(id));
    }

    @Operation(
            summary = "Criar barbeiro",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                        "nome": "Ninguem",
                        "telefone": "(11) 99999-9999",
                        "unidade": "UNIDADE1"
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Barbeiro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Campos inválidos ou ausentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ValidationErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 400,
                    "error": "Erro de Validação",
                    "message": "Um ou mais campos estão inválidos",
                    "path": "/api/barbeiro",
                    "timestamp": "2026-03-21T10:00:00",
                    "fields": [
                        {
                            "fields": "nome",
                            "message": "não deve estar em branco",
                            "rejectValue": ""
                        },
                        {
                            "fields": "unidade",
                            "message": "não deve ser nulo",
                            "rejectValue": null
                        }
                    ]
                }
                """))),
            @ApiResponse(responseCode = "415", description = "Content-Type não suportado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 415,
                    "error": "Content-Type não Suportado",
                    "message": "O Content-Type 'text/plain' não é suportado. Use 'application/json'.",
                    "path": "/api/barbeiro",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> criar(@Valid @RequestBody BarbeiroRequestDTO dto) {
        return ResponseEntity.status(201).body(barbeiroService.criar(dto));
    }

    @Operation(
            summary = "Atualizar barbeiro parcialmente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                        "nome": "Deltrano"
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 404,
                    "error": "Não Encontrado",
                    "message": "Barbeiro não encontrado(a) com id: 5",
                    "path": "/api/barbeiro/5",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "405", description = "Método não permitido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 405,
                    "error": "Método não Permitido",
                    "message": "O método 'POST' não é suportado para esta rota.",
                    "path": "/api/barbeiro/5",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> atualizar(@PathVariable Long id,
                                                         @RequestBody BarbeiroUpdateDTO dto) {
        return ResponseEntity.ok(barbeiroService.atualizar(id, dto));
    }

    @Operation(summary = "Deletar barbeiro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Barbeiro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 404,
                    "error": "Não Encontrado",
                    "message": "Barbeiro não encontrado(a) com id: 9",
                    "path": "/api/barbeiro/9",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "405", description = "Método não permitido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}