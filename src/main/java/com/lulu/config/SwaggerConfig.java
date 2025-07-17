package com.lulu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🌸 Florería Virtual API")
                        .description("Sistema de E-commerce para venta de flores")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Florería Virtual Team")
                                .email("contacto@floreria.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo Local"),
                        new Server()
                                .url("https://tu-app-railway.up.railway.app")
                                .description("Servidor de Producción (Railway)")
                ));
    }
}
