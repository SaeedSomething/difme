package com.example.difme.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

import com.example.difme.annotation.ApiResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "bearerAuth", description = "JWT Token Authorization", scheme = "bearer", bearerFormat = "JWT")
public class OpenAPIConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    // New environment-driven configurations
    @Value("${swagger.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${swagger.server.description:Server URL in Development environment}")
    private String serverDescription;

    @Value("${swagger.contact.email:info@example.org}")
    private String contactEmail;

    @Value("${swagger.contact.name:API Support}")
    private String contactName;

    @Value("${swagger.contact.url:https://www.example.org}")
    private String contactUrl;

    @Value("${swagger.info.title:Difme Backend API}")
    private String apiTitle;

    @Value("${swagger.info.version:1.0}")
    private String apiVersion;

    @Value("${swagger.info.description:This API exposes endpoints for Difme }")
    private String apiDescription;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(serverUrl + contextPath);
        devServer.setDescription(serverDescription);

        Contact contact = new Contact();
        contact.setEmail(contactEmail);
        contact.setName(contactName);
        contact.setUrl(contactUrl);

        License mitLicense = new License().name("MITLicense").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title(apiTitle)
                .version(apiVersion)
                .contact(contact)
                .description(apiDescription)
                .license(mitLicense);

        // Add security requirement for JWT
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                .addSecurityItem(securityRequirement);
    }
}