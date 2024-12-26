package com.rest.ticketing_rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ticketing App API Docs",
                description = "Rest Api Documentation for Ticketing Project",
                version = "v1",
                contact = @Contact(
                        name="Moonstar",
                        email = "support@moonstar.com",
                        url= "https://www.moonstar.com"
                )
        ),
        security = {@SecurityRequirement(name="Keycloak")}
)
@SecurityScheme(
      name = "Keycloak",
      type= SecuritySchemeType.OAUTH2,
      flows = @OAuthFlows(authorizationCode = @OAuthFlow(
              authorizationUrl = "http://localhost:8080/realms/moonstar-dev/protocol/openid-connect/auth",
              tokenUrl = "http://localhost:8080/realms/moonstar-dev/protocol/openid-connect/token"
      ))
)
public class SwaggerConfig {

}
