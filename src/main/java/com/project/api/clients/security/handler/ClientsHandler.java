package com.project.api.clients.security.handler;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.project.api.clients.security.dto.AdvancedFilterDTO;
import com.project.api.clients.security.dto.ClientsDTO;
import com.project.api.clients.security.exceptions.CustomException;
import com.project.api.clients.security.services.IClientsServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
@Tag(name = "Clients")
public class ClientsHandler {
	
	@Autowired
	private IClientsServices clientsService;
	private static Logger LOGGER = LoggerFactory.getLogger(UsersHandler.class);
	
	public Mono<ServerResponse> listHandler(ServerRequest serverRequest)  {
		try{
			LOGGER.info("######### - Se hace el llamado al servicio clientList - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					clientsService.list(),
					ClientsDTO.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}
	
	public Mono<ServerResponse> searchHandler(ServerRequest serverRequest)  {
		try{
			LOGGER.info("######### - Se hace el llamado al servicio clientSearch - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					clientsService.search(serverRequest.queryParam("alias").get()),
					ClientsDTO.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}
	
	public Mono<ServerResponse> advancedSearchHandler(ServerRequest serverRequest)  {
		try{
			LOGGER.info("######### - Se hace el llamado al servicio clientAdvancedSearch - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					clientsService.advancedSearch(serverRequest.bodyToMono(AdvancedFilterDTO.class).toFuture().get()),
					ClientsDTO.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}
	
	public Mono<ServerResponse> createdHandler(ServerRequest serverRequest) {
		try {
			LOGGER.info("######### - Se hace el llamado al servicio clientCreated - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					clientsService.created(serverRequest.bodyToMono(ClientsDTO.class).toFuture().get()),
					ClientsDTO.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}
	
	public Mono<ServerResponse> modifiedHandler(ServerRequest serverRequest) {
		try {
			LOGGER.info("######### - Se hace el llamado al servicio clientModified - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					clientsService.modified(serverRequest.bodyToMono(ClientsDTO.class).toFuture().get()),
					ClientsDTO.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}
	
	public Mono<ServerResponse> deleteHandler(ServerRequest serverRequest) {
		try {
			LOGGER.info("######### - Se hace el llamado al servicio clientDeleted - #########");
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
					clientsService.delete(Long.parseLong(serverRequest.queryParam("id").get())),
					ClientsDTO.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}
	
	public Mono<ServerResponse> exportHandler(ServerRequest serverRequest) {
		try {
			LOGGER.info("######### - Se establece un nombre aleatorio para el archivo CSV - #########");
			String fileName = String.format("%s.csv", RandomStringUtils.randomAlphabetic(10));
			LOGGER.info("######### - Se hace el llamado al servicio export - #########");
			return ServerResponse.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=" + fileName)
	                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(
					clientsService.export(serverRequest.bodyToFlux(ClientsDTO.class).collectList().toFuture().get())
					.flatMap(x -> {
                        Resource resource = new InputStreamResource(x);
                        return Mono.just(resource);
                    }), Resource.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servicio", 500));
		}
	}

}
