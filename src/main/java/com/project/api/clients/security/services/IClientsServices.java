package com.project.api.clients.security.services;

import java.io.ByteArrayInputStream;
import java.util.List;
import com.project.api.clients.security.dto.AdvancedFilterDTO;
import com.project.api.clients.security.dto.ClientsDTO;
import com.project.api.clients.security.entities.Clients;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientsServices {

	Flux<ClientsDTO> list();
	Flux<ClientsDTO> search(String alias);
	Flux<ClientsDTO> advancedSearch(AdvancedFilterDTO advancedSearch);
	Mono<Clients> created(ClientsDTO clientDTO);
	Mono<Clients> modified(ClientsDTO clientDTO);
	Mono<Void> delete(long id);
	Mono<ByteArrayInputStream> export(List<ClientsDTO> clients);
}
