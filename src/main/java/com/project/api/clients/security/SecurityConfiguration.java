package com.project.api.clients.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.project.api.clients.security.constants.EndPoints;
import com.project.api.clients.security.jwt.JwtEntryPoint;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity  
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	@Autowired
	private SecurityContextRepository securityContextRepository;
	
	@SuppressWarnings("removal")
	@Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtEntryPoint jwtFilter) {
        return http
        		
        		.authorizeExchange()
        		.pathMatchers(EndPoints.WEBJARS_PATH + "/**").permitAll()
        		.pathMatchers(EndPoints.OPENAPI_DOCS_PATH + "/**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH.concat(EndPoints.USERS_PATH.concat(EndPoints.LOGIN.concat("/**")))).permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.LIST + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.LIST + "**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.CREATED + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.CREATED + "**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.MODIFIED + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.MODIFIED + "**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.DELETED + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.DELETED + "**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.SEARCH + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.SEARCH + "**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.ADVANCED_SEARCH + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.ADVANCED_SEARCH + "**").permitAll()
                .pathMatchers(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.EXPORT + "/**", EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.EXPORT + "**").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(securityContextRepository)
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .csrf().disable()
                .cors(cors -> cors.disable())
                .build();
    }
	
}
