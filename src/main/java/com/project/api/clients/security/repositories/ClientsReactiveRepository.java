package com.project.api.clients.security.repositories;

import java.time.LocalDateTime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.project.api.clients.security.entities.Clients;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClientsReactiveRepository extends ReactiveCrudRepository<Clients, Long> {
	
	Mono<Clients> findByEmail(String email);
	Flux<Clients> findBySharedKeyContaining(String sharedKey);
	Flux<Clients> findByBussinessIdContaining(String bussinessId);
	Flux<Clients> findByEmailContaining(String email);
	Flux<Clients> findByPhoneContaining(String phone);
	Flux<Clients> findByDataAddedBetween(LocalDateTime startDate, LocalDateTime endDate);

}
