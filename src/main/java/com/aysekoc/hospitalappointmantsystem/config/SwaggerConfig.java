package com.aysekoc.hospitalappointmantsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI baseOpenAPI() {

        Components components = new Components();

        // örnek response tanımları
        components.addResponses("badRequest", new ApiResponse().description("Bad Request"));
        components.addResponses("internalServerError", new ApiResponse().description("Internal Server Error"));
        components.addResponses("successfulResponse", new ApiResponse().description("Successful Response"));

        return new OpenAPI()
                .components(components)
                .info(new Info()
                        .title("Hospital Appointment System API")
                        .version("1.0.0")
                        .description("Hospital Appointment System OpenAPI Documentation"));
    }
}
