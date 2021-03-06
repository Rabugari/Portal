package br.com.portal.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuração para o Swagger
 * 
 * @author douglas.takara
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apiDoc() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.portal.controller")).paths(PathSelectors.any())
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Portal",
				"Um simples portal de serviços para autenticação e cadastro de usuário, via tokens JWT - JSON Web Token.",
				"0.0.1-SNAPSHOT", null,
				new springfox.documentation.service.Contact("Douglas Takara",
						"https://www.linkedin.com/in/douglas-massao-takara/", "takaramassao@hotmail.com"),
				"Apache Licence 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
	}
}
