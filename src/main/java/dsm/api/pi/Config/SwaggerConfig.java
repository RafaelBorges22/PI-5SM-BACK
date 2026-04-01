package dsm.api.pi.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Barbearia")
                        .description("Gerenciamento de serviços, barbeiros e clientes")
                        .version("1.0.0"))
                .components(new Components()
                        .addSchemas("ErrorResponse", new Schema<>()
                                .type("object")
                                .addProperty("status", new Schema<>().type("integer").example(404))
                                .addProperty("error", new Schema<>().type("string").example("Não Encontrado"))
                                .addProperty("message", new Schema<>().type("string").example("Barbeiro não encontrado(a) com nome: Fulano"))
                                .addProperty("path", new Schema<>().type("string").example("/api/servicos"))
                                .addProperty("timestamp", new Schema<>().type("string").example("2026-03-21T10:00:00"))
                        )
                        .addSchemas("ValidationErrorResponse", new Schema<>()
                                .type("object")
                                .addProperty("status", new Schema<>().type("integer").example(400))
                                .addProperty("error", new Schema<>().type("string").example("Erro de Validação"))
                                .addProperty("message", new Schema<>().type("string").example("Um ou mais campos estão inválidos"))
                                .addProperty("path", new Schema<>().type("string").example("/api/servicos"))
                                .addProperty("timestamp", new Schema<>().type("string").example("2026-03-21T10:00:00"))
                                .addProperty("fields", new Schema<>().type("array").items(
                                        new Schema<>().type("object")
                                                .addProperty("fields", new Schema<>().type("string").example("nomeCliente"))
                                                .addProperty("message", new Schema<>().type("string").example("não deve estar em branco"))
                                                .addProperty("rejectValue", new Schema<>().type("string").example(""))
                                ))
                        )
                );
    }
}