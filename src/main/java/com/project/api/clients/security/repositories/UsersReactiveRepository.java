package com.project.api.clients.security.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.project.api.clients.security.entities.Users;
import reactor.core.publisher.Mono;

@Repository
public interface UsersReactiveRepository extends ReactiveCrudRepository<Users, Long> {
	
	Mono<Users> findByEmail(String email);

}
