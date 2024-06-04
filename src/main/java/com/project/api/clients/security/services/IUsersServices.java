package com.project.api.clients.security.services;

import java.util.concurrent.ExecutionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.api.clients.security.dto.LoginDTO;
import com.project.api.clients.security.dto.TokenDTO;
import com.project.api.clients.security.entities.Users;
import com.project.api.clients.security.jwt.JwtProvider;
import reactor.core.publisher.Mono;

public interface IUsersServices {

	Mono<TokenDTO> generateToken(JwtProvider jwtProvider, PasswordEncoder passwordEncoder, LoginDTO login) throws InterruptedException, ExecutionException;
	Mono<Users> findByEmail(String email);
}
