package com.fiveguys.RIA.RIA_Backend.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {

    SecurityScheme scheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");

    SecurityRequirement requirement = new SecurityRequirement()
        .addList("JWT");

    return new OpenAPI()
        .addSecurityItem(requirement)
        .components(new Components().addSecuritySchemes("JWT", scheme));
  }
}
