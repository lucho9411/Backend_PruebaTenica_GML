package com.project.api.clients.security.handler;

import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.project.api.clients.security.dto.LoginDTO;
import com.project.api.clients.security.dto.TokenDTO;
import com.project.api.clients.security.entities.Users;
import com.project.api.clients.security.exceptions.CustomException;
import com.project.api.clients.security.jwt.JwtProvider;
import com.project.api.clients.security.services.IUsersServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
@Tag(name = "Users")
public class UsersHandler {
	
	@Autowired
	private IUsersServices usersServices;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static Logger LOGGER = LoggerFactory.getLogger(UsersHandler.class);
	
	public Mono<ServerResponse> findByEmailHandler(String email)  {
		try{
			LOGGER.info("######### - Se hace el llamado al servicio findByEmail - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					usersServices.findByEmail(email),
					Users.class);
		}
		catch(UsernameNotFoundException ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado", 404)); 
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}

	public Mono<ServerResponse> loginHandler(ServerRequest serverRequest)  {
		try {
			LOGGER.info("######### - Se hace el llamado al servicio generateToken - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
	        		usersServices.generateToken(
	        				jwtProvider,
	        				passwordEncoder,
	        				serverRequest.bodyToMono(LoginDTO.class).toFuture().get()
	        		),
	        		TokenDTO.class);
		}
		catch (BadCredentialsException bce) {
			bce.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(bce.getMessage());
			return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Usuario y/o clave inválidos", 400));
		}
		catch(InterruptedException ie) {
			ie.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ie.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
		catch(ExecutionException ee) {
			ee.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ee.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
		catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(e.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}

}
