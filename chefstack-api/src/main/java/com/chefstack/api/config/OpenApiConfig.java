package com.chefstack.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// swagger + auth bearer
@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_BEARER = "bearer-jwt";

    @Bean
    public OpenAPI chefstackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChefStack API")
                        .description("API REST de gestion de recetas: categorias, recetas, ingredientes y favoritos")
                        .version("1.0.0")
                        .contact(new Contact().name("ChefStack")))
                .components(new Components().addSecuritySchemes(ESQUEMA_BEARER,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token de Firebase. Pega el ID token para probar los endpoints protegidos.")));
    }
}
