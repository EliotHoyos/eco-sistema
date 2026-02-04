package com.example.eco_sistema.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eco Sistema API")
                        .version("1.0.0")
                        .description("API del sistema de gestión Eco Sistema")
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación externa")
                        .url("https://springdoc.org/"));
    }
}
