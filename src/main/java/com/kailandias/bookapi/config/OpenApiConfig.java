package com.kailandias.bookapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookApiDefinition() {
        return new OpenAPI().info(new Info()
                .title("Book API")
                .description("API REST para gerenciamento de livros (cadastro, consulta, atualizacao e exclusao).")
                .version("1.0")
                .contact(new Contact().name("Kailandias")));
    }
}
