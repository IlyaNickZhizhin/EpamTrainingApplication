package org.epam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi dafault(){
        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan("org.epam")
                .pathsToMatch("/*").build();
    }

    @Bean
    public OpenAPI api(){
        return new OpenAPI().info(
                new Info()
                        .title("EpamTrainingApplication Rest API")
                        .description("created by the intern of Epam Training Center")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact().name("Ilya Zhizhin").email("ilya.zhizhin@epam.com").url("github"))
                        .license(new License().name("none").url("none")));
    }

    @Bean
    public SpringDocConfigProperties springDocConfigProperties(){
        SpringDocConfigProperties prop = new SpringDocConfigProperties();
        SpringDocConfigProperties.ApiDocs api = new SpringDocConfigProperties.ApiDocs();
        api.setPath("swagger");
        prop.setApiDocs(api);
        return prop;
    }

    @Bean
    public SwaggerUiOAuthProperties swaggerUiOAuthProperties() {
        return new SwaggerUiOAuthProperties();
    }
}
