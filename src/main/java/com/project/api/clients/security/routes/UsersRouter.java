package com.project.api.clients.security.routes;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.project.api.clients.security.constants.EndPoints;
import com.project.api.clients.security.dto.LoginDTO;
import com.project.api.clients.security.dto.TokenDTO;
import com.project.api.clients.security.handler.UsersHandler;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Configuration
@Slf4j
public class UsersRouter {
	
	@Bean
	@RouterOperations({
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.USERS_PATH + EndPoints.LOGIN + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.POST,
                beanClass = UsersHandler.class,
                beanMethod = "loginHandler",
                operation = @Operation(
                        operationId = "loginHandler",
                        requestBody = 
                        		@RequestBody(required = true, description = "Por favor ingresar la información del login en Base64",
                                        content = @Content(
                                                schema = @Schema(implementation = LoginDTO.class))),
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Login exitoso",
                                       content = @Content(schema = @Schema(
                                               implementation = TokenDTO.class
                                       ))
                               )
                        }
                )
        )
    })
	RouterFunction<ServerResponse> usersRouters(UsersHandler usersHandler){
		 return RouterFunctions.route()
	                .POST(EndPoints.GLOBAL_PATH.concat(EndPoints.USERS_PATH + EndPoints.LOGIN + "/"), usersHandler::loginHandler)
	                .build();
	}

}
