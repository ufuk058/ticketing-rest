package com.rest.ticketing_rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
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
        )
)
public class SwaggerConfig {

}
