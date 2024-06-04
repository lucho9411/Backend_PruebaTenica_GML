package com.project.api.clients.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.project.api.clients.security.jwt.JwtAuthenticationFilter;
import com.project.api.clients.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository{
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private JwtProvider jwtProvider;
	private static Logger LOGGER = LoggerFactory.getLogger(SecurityContextRepository.class);

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
    	LOGGER.info("######### - Se obtiene el token del exchange de la petición - #########");
    	LOGGER.info(exchange.getAttribute("token"));
        String token = exchange.getAttribute("token");
        LOGGER.warn("######### - Se retorna el SecurityContext - #########");
        return token==null?jwtAuthenticationFilter.authenticate(new UsernamePasswordAuthenticationToken(token, token))
        		.map(SecurityContextImpl::new):
        		Mono.just(jwtProvider.getAuthentication(token))
        		.subscribeOn(Schedulers.boundedElastic())
        		.map(SecurityContextImpl::new);
    }

}
