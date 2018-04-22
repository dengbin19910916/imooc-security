package com.imooc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ImoocSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImoocSecurityDemoApplication.class, args);
    }

    @Bean
    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Imooc Security Demo")
                .description("Security示例")
                .termsOfServiceUrl("http://localhost/user")
                .contact(new Contact("Dengbin", "", ""))
                .version(version)
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc"))
                .paths(PathSelectors.any())
                .build();
    }

    @Value("${info.version}")
    private String version;
}
