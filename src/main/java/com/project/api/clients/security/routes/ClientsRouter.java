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
import com.project.api.clients.security.dto.AdvancedFilterDTO;
import com.project.api.clients.security.dto.ClientsDTO;
import com.project.api.clients.security.entities.Clients;
import com.project.api.clients.security.handler.ClientsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ClientsRouter {
	
	@Bean
	@RouterOperations({
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.LIST + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.GET,
                beanClass = ClientsHandler.class,
                beanMethod = "listHandler",
                operation = @Operation(
                        operationId = "listHandler",
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Listado de Clientes",
                                       content = @Content(schema = @Schema(
                                               implementation = ClientsDTO.class
                                       ))
                               )
                        }
                )
        ),
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.SEARCH + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.GET,
                beanClass = ClientsHandler.class,
                beanMethod = "searchHandler",
                operation = @Operation(
                        operationId = "searchHandler",
                        parameters = 
                     		 	@Parameter(
                     		 			in = ParameterIn.PATH,
                     		 			name = "alias",
                     		 			description = "Por favor ingresar el alias de los clientes a filtrar",
                     		 			required = true,
                     		 			schema = 
                     		 			@Schema(
                     		 					type = "string"
                     		 			)
                     		 	)
                        ,
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Listado de Clientes con el Alias",
                                       content = @Content(schema = @Schema(
                                               implementation = ClientsDTO.class
                                       ))
                               )
                        }
                )
        ),
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.ADVANCED_SEARCH + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.GET,
                beanClass = ClientsHandler.class,
                beanMethod = "advancedSearchHandler",
                operation = @Operation(
                        operationId = "advancedSearchHandler",
                		requestBody = 
                         		@RequestBody(required = true, description = "Por favor ingresar la informaión de los filtros de clientes",
                                         content = @Content(
                                                 schema = @Schema(implementation = AdvancedFilterDTO.class))),
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Listado de Clientes con los filtros seleccionados",
                                       content = @Content(schema = @Schema(
                                               implementation = ClientsDTO.class
                                       ))
                               )
                        }
                )
        ),
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.CREATED + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.POST,
                beanClass = ClientsHandler.class,
                beanMethod = "createdHandler",
                operation = @Operation(
                        operationId = "createdHandler",
                        		 requestBody = 
                         		@RequestBody(required = true, description = "Por favor ingresar la informaión del cliente a crear",
                                         content = @Content(
                                                 schema = @Schema(implementation = ClientsDTO.class))),
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Cliente creado correctamente",
                                       content = @Content(schema = @Schema(
                                               implementation = Clients.class
                                       ))
                               )
                        }
                )
        ),
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.MODIFIED + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.PUT,
                beanClass = ClientsHandler.class,
                beanMethod = "modifiedHandler",
                operation = @Operation(
                        operationId = "modifiedHandler",
                        		 requestBody = 
                         		@RequestBody(required = true, description = "Por favor ingresar la informaión del cliente a modificar",
                                         content = @Content(
                                                 schema = @Schema(implementation = ClientsDTO.class))),
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Cliente modificado correctamente",
                                       content = @Content(schema = @Schema(
                                               implementation = Clients.class
                                       ))
                               )
                        }
                )
        ),
		@RouterOperation(
                path = EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.DELETED + "/",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.DELETE,
                beanClass = ClientsHandler.class,
                beanMethod = "deleteHandler",
                operation = @Operation(
                        operationId = "deleteHandler",
	            		 parameters = 
	            		 	@Parameter(
	            		 			in = ParameterIn.PATH,
	            		 			name = "id",
	            		 			description = "Por favor ingresar el id del cliente a eliminar",
	            		 			required = true),
                        responses = {
                               @ApiResponse(
                                       responseCode = "200",
                                       description = "Cliente eliminado correctamente",
                                       content = @Content(schema = @Schema(
                                               implementation = Clients.class
                                       ))
                               )
                        }
                )
        )
    })
	RouterFunction<ServerResponse> clientsRouters(ClientsHandler clientsHandler){
		 return RouterFunctions.route()
	                .GET(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.LIST + "/", clientsHandler::listHandler)
	                .GET(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.SEARCH + "/", clientsHandler::searchHandler)
	                .POST(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.ADVANCED_SEARCH + "/", clientsHandler::advancedSearchHandler)
	                .POST(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.CREATED + "/", clientsHandler::createdHandler)
	                .PUT(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.MODIFIED + "/", clientsHandler::modifiedHandler)
	                .DELETE(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.DELETED + "/", clientsHandler::deleteHandler)
	                .POST(EndPoints.GLOBAL_PATH + EndPoints.CLIENTS_PATH + EndPoints.EXPORT + "/", clientsHandler::exportHandler)
	                .build();
	}

}
