package com.project.api.clients.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import java.util.stream.Stream;
import com.project.api.clients.security.constants.EndPoints;
import com.project.api.clients.security.exceptions.CustomException;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;

@Component
public class JwtEntryPoint implements WebFilter{
	
	@Autowired
	private JwtProvider jwtProvider;
	private static Logger LOGGER = LoggerFactory.getLogger(JwtEntryPoint.class);
	
	@Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        LOGGER.info("######### - Se obtiene el path - #########");
        System.out.println(request.getHeaders());
        String path = request.getPath().value();
        LOGGER.info("######### - Se validan los paths accesibles sin authorización - #########");
        if (Stream.of(
        		EndPoints.LOGIN,
        		EndPoints.WEBJARS_PATH,
        		EndPoints.OPENAPI_DOCS_PATH,
        		EndPoints.CLIENTS_PATH)
        	.anyMatch(path::contains)) {
        	 	return chain.filter(exchange);
        }
        else {
        	LOGGER.info("######### - Se obtiene la authorización - #########");
        	String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        	LOGGER.info("######### - El valor del header Authorization es: " + auth + " - #########");
        	LOGGER.info("######### - Se valida si la authorización es nula - #########");
        	if(auth == null) {
        		LOGGER.error("######### - Error - #########");
        		LOGGER.error("######### - Token no encontrado - #########");
        		return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Token no encontrado", 500));
        	}
        	else {
        		LOGGER.info("######### - Se valida si la authorización no es de tipo Bearer válido - #########");
        		if(!auth.startsWith("Bearer ")) {
        			LOGGER.error("######### - Error - #########");
            		LOGGER.error("######### - Token inválido - #########");
        			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Token inválido", 500));
        		}
        		else {
        			LOGGER.info("######### - Se obtiene el token de la authorización - #########");
        			String token = auth.replace("Bearer ", "");
        			String validateToken = jwtProvider.validateTokenMessage(token);
        			LOGGER.info("######### - Se validan todos los filtros del token - #########");
        			if (validateToken != "Ok") {
        				LOGGER.error("######### - Error - #########");
                		LOGGER.error("######### - " + validateToken + " - #########");
        				return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, validateToken, 500));
        			}
        			else {
        				LOGGER.info("######### - Se agrega el token a los atributos del servicio - #########");
            	        exchange.getAttributes().put("token", token);
            	        exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "*");
            	        return chain.filter(exchange); 
        			}
        		}
        	}
        } 
    }
}
