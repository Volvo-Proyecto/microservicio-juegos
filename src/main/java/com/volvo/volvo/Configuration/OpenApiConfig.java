package com.volvo.volvo.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public  OpenAPI apiInfo(){
        return new OpenAPI()
        .info(new Info()
        .title("Microservicio de Juegos")
        .version("0.0.3")
        .description("Microservicio enfocado en agregar el catalogo de juegos y unirlos a genero plataforma y estudio"));

    }

}
