package com.example.rabbithell.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI deliveryAppOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("RabbitHell API")
                .description("팀 프로젝트 - RabbitHell")
                .version("v0.1.0")
                .contact(new Contact()
                    .name("RabbitHell")
                    .email("giwon.git@gmail.com"))
                .license(new License()
                    .name("라이센스 토의 후 작성 예정")
                    .url("라이센스 토의 후 작성 예정")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                ))
            .externalDocs(new ExternalDocumentation()
                .description("깃허브 레포지토리")
                .url("https://github.com/giwon-wg/RabbitHell"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("RabbitHell API")
            .pathsToMatch("/**")
            .build();
    }
}
