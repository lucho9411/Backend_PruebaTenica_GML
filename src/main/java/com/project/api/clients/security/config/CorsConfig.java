package com.project.api.clients.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import com.project.api.clients.security.constants.Headers;
import com.project.api.clients.security.constants.HttpMethods;

@Configuration
@EnableWebFlux
public class CorsConfig implements WebFluxConfigurer {
	
	 @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedMethods(HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT, HttpMethods.DELETE)
	                .allowedOriginPatterns("*")
	                .allowedHeaders(Headers.ORIGIN, Headers.CONTENT_TYPE, Headers.ACCEPT,
	                				Headers.AUTHORIZATION, Headers.COOKIE,
	                				Headers.ACCESS_CONTROL_CREDENTIALS,
	                				Headers.ACCESS_CONTROL_ORIGIN)
	    	        .allowedOrigins("http://localhost:4200")
	    	        .maxAge(3600)
	                .allowCredentials(true);
	    }
}