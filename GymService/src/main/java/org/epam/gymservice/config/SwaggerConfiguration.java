package org.epam.gymservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api(){
        Server server = new Server();
        server.setUrl("https://ilya.zhizhin.xyz");
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .info(
                new Info()
                        .title("EpamTrainingApplication Rest API")
                        .description("created by the intern of Epam Training Center")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact().name("Ilya Zhizhin").email("ilya_zhizhin@epam.com").url("https://github.com/IlyaNickZhizhin"))
                        .license(new License().name("none").url("none")));
    }

}
