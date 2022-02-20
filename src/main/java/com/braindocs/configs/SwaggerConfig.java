package com.braindocs.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.braindocs.controllers"))
                .paths(PathSelectors.regex("/api.*"))
                //.paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("BrainDocs",
                "Document managment system",
                "v1",
                "",
                "polyashofff@yandex.ru",
                //heroku не билдит проект, если используется другой конструктор
                //new Contact("Алексей Поляшов","https://alexey-polyashov.github.io/brain-docs/", "polyashofff@yandex.ru"),
                "",
                "");
    }


}