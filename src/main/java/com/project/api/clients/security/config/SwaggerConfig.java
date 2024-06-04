package com.project.api.clients.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
					.info(new Info()
								.title("Documentación del CRUD de Clientes Alianza")
								.version("1.0")
								.description("Documentación del CRUD de Clientes Alianza Con OpenAPI Swagger"));
	}

}
