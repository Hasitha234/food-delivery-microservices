package com.fooddelivery.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Food Delivery Payment Service API")
                        .description("Microservice responsible for handling payments in the online food ordering system.")
                        .version("1.0.0")
                        .contact(new Contact().name("Shenal - Payment Service"))
                        .license(new License().name("Assignment Use Only")));
    }
}
