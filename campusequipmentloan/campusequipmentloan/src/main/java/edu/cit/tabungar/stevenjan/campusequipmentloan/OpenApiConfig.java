package edu.cit.tabungar.stevenjan.campusequipmentloan;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Campus Equipment Loan System API")
                        .description("REST API for managing equipment loans in a campus environment")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Campus Equipment Loan System")
                                .email("support@campus.edu"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
