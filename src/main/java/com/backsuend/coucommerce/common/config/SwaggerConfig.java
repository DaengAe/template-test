package com.backsuend.coucommerce.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition(
	servers = {
		@Server(url = "http://localhost:8080", description = "로컬 서버")
	})
@Configuration
@SecurityScheme(
	name = "Authorization",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT",
	description = "Access Token을 입력해주세요. (앞에 'Bearer ' 붙여야 함)"
)
@SecurityScheme(
	name = "Refresh-Token",
	type = SecuritySchemeType.APIKEY,
	in = SecuritySchemeIn.HEADER,
	paramName = "Refresh-Token",
	description = "Refresh Token 원본 값을 입력해주세요. ('Bearer ' 없이)"
)
public class SwaggerConfig {
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.components(new Components())
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("Cou-commerce Swagger")
			.description("모든 REST API")
			.version("1.0.0");
	}
}