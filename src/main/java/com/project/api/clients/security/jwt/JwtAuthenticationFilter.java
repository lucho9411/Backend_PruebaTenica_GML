package com.project.api.clients.security.jwt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.project.api.clients.security.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements ReactiveAuthenticationManager {
	
	@Autowired
	private JwtProvider jwtProvider;
	private static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@SuppressWarnings("unchecked")
	@Override
    public Mono<Authentication> authenticate(Authentication authentication) {
		LOGGER.info("######### - Se inicia la autenticación del usuario - #########");
		 return Mono.just(authentication)
            .map(auth -> jwtProvider.getClaims(auth.getCredentials().toString()))
            .log()
            .onErrorResume(e -> Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "Token vacío", 401)))
            .map(claims -> new UsernamePasswordAuthenticationToken(
            		authentication.getPrincipal(),
                    null,
                    Stream.of(claims.get("Authority"))
                            .map(role -> (List<Map<String, String>>) role)
                            .flatMap(role -> role.stream()
                                    .map(r -> r.get("authority"))
                                    .map(SimpleGrantedAuthority::new))
                            .collect(Collectors.toList())
            ));
    }
	
	@SuppressWarnings("unused")
	private String getToken(HttpServletRequest request) {
		LOGGER.info("######### - Se obtiene la authotización de los headers de la petición - #########");
		String header = request.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer")) {
			LOGGER.info("######### - Authorización válida y se retorna el token - #########");
			return header.replace("Bearer", "");
		}
		else {
			LOGGER.warn("######### - Authorización inválida y se retorna nulo - #########");
			return null;
		}
	}
	
	public String getToken(String bearer) {
		if(bearer != null) {
			return bearer.replace("Bearer ", "");
		}
		else {
			return null;
		}
	}

}
