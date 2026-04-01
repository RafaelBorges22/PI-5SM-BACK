package dsm.api.pi.Controller;


import dsm.api.pi.DTO.Servico.ServicoResponseDTO;
import dsm.api.pi.Service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
@Tag(name = "Serviços", description = "Gerenciamento de serviços da barbearia")
public class ServicoController {

    private final ServicoService servicoService;

    @Operation(summary = "Listar todos os serviços")
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
                    "path": "/api/servicos",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """)))
    })
    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    @Operation(summary = "Buscar serviços de hoje")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serviços do dia retornados"),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @GetMapping("/hoje")
    public ResponseEntity<Object> buscarServicosHoje() {
        return ResponseEntity.ok(servicoService.buscarServicosHoje());
    }

    @Operation(
            summary = "Criar serviço",
            description = "Aceita pagamento simples (cartão/dinheiro) ou PIX",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Cartão/Dinheiro",
                                            summary = "Pagamento simples",
                                            value = """
                        {
                            "valor": 50.00,
                            "dataServico": "2026-03-21",
                            "nomeCliente": "João Silva",
                            "nomeBarbeiro": "Fulano",
                            "statusPagamento": "PAGO",
                            "metodoPagamento": "CARTAO_DEBITO",
                            "produto": "Gel",
                            "servico": "Corte de Cabelo"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "PIX",
                                            summary = "Pagamento via PIX",
                                            value = """
                        {
                            "data": {
                                "valor": 50.00,
                                "dataServico": "2026-03-21",
                                "nomeCliente": "Deltrano",
                                "nomeBarbeiro": "Ninguem",
                                "statusPagamento": "PENDENTE",
                                "metodoPagamento": "PIX",
                                "produto": "Pomada Modeladora",
                                "servico": "Corte + Barba"
                            },
                            "pix": {
                                "chave": "31b007ea-f1f0-48be-a72d-67ed74ddd8d2",
                                "valor": "50.00"
                            }
                        }
                        """
                                    )
                            }
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serviço criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "JSON inválido ou enum incorreto",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 400,
                    "error": "JSON Inválido",
                    "message": "Valor inválido para o campo 'metodoPagamento': 'FIADO' não é um valor aceito.",
                    "path": "/api/servicos",
                    "timestamp": "2026-03-21T10:00:00",
                    "fields": [
                        {
                            "fields": "metodoPagamento",
                            "message": "Valor inválido para o campo 'metodoPagamento': 'FIADO' não é um valor aceito.",
                            "rejectValue": "CREDITO"
                        }
                    ]
                }
                """))),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 400,
                    "error": "Não Encontrado",
                    "message": "Barbeiro não encontrado(a) com nome: Fulano",
                    "path": "/api/servicos",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criarServico(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(servicoService.criarServico(body));
    }

    @Operation(summary = "Deletar serviço por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Serviço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse"),
                            examples = @ExampleObject(value = """
                {
                    "status": 404,
                    "error": "Não Encontrado",
                    "message": "Serviço não encontrado com id: 99",
                    "path": "/api/servicos/99",
                    "timestamp": "2026-03-21T10:00:00"
                }
                """))),
            @ApiResponse(responseCode = "405", description = "Método não permitido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "ErrorResponse")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}