package com.items.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI itemManagementAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(securityComponents());
    }

    private Info apiInfo() {
        return new Info()
                .title("Item Management API")
                .description("REST API para gestión de items")
                .version("v1.0");
    }

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes("JWT", jwtScheme());
    }

    private SecurityScheme jwtScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}