package com.project.api.clients.security.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import com.project.api.clients.security.dto.AdvancedFilterDTO;
import com.project.api.clients.security.dto.ClientsDTO;
import com.project.api.clients.security.entities.Clients;
import com.project.api.clients.security.exceptions.CustomException;
import com.project.api.clients.security.repositories.ClientsReactiveRepository;
import com.project.api.clients.security.utilities.ByteArrayInOutStream;
import com.project.api.clients.security.utilities.Utility;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ClientsServicesReactiveJPA implements IClientsServices {
	
	@Autowired
	private ClientsReactiveRepository clientsRepo;
	@Autowired
    private ModelMapper modelMapper;
	private static Logger LOGGER = LoggerFactory.getLogger(ClientsServicesReactiveJPA.class);
	
	// ---------------- CRUD Functions ------------------------\\

	@Override
	public Flux<ClientsDTO> list() {
		LOGGER.info("######### - Listando todos los clientes - #########");
		return clientsRepo.findAll()
			.groupBy(client -> client.getId())
			.flatMap(group ->
            	group.collectList()
                    .map(clients -> {
                    	LOGGER.info("######### - Convirtiendo las ClientsEntity en ClientsDTO - #########");
                    	return mapEntityToDto(clients.get(0));
                    })
            )
			.delayElements(Duration.ofMillis(100))
			.subscribeOn(Schedulers.parallel());
	}
	
	@Override
	public Flux<ClientsDTO> search(String alias) {
		LOGGER.info("######### - Buscando y filtrando clientes por su alias - #########");
		return clientsRepo.findBySharedKeyContaining(alias)
				.groupBy(client -> client.getId())
				.flatMap(group ->
	            	group.collectList()
	                    .map(clients -> {
	                    	LOGGER.info("######### - Convirtiendo las ClientsEntity en ClientsDTO - #########");
	                    	return mapEntityToDto(clients.get(0));
	                    })
	            )
				.delayElements(Duration.ofMillis(100))
				.subscribeOn(Schedulers.parallel());
	}
	
	@Override
	public Flux<ClientsDTO> advancedSearch(AdvancedFilterDTO advancedSearchDTO) {
		LOGGER.info("######### - Buscando y filtrando clientes por los filtros avanzados - #########");
		List<Clients> clients = new ArrayList<Clients>();
		List<ClientsDTO> clientsDTO = new ArrayList<ClientsDTO>();
		try {
			clients = clientsRepo.findAll()
					.publishOn(Schedulers.boundedElastic())
					.delayElements(Duration.ofMillis(100))
					.subscribeOn(Schedulers.parallel())
					.collectList()
					.toFuture().get();
			LOGGER.info("######### - Se obtuvieron todo el listado de clientes y se inicia su recorrido - #########");
			for(Clients client: clients) {
				ClientsDTO clientDTO = advancedSearchParams(advancedSearchDTO, client);
				if (clientDTO != null) {
					LOGGER.info("######### - Se llena la lista de ClientsDTO - #########");
					clientsDTO.add(clientDTO);
				}
			}
			LOGGER.info("######### - Se retorna el Flux de ClientsDTO - #########");
			return Flux.fromIterable(clientsDTO)
					.delayElements(Duration.ofMillis(300))
					.subscribeOn(Schedulers.parallel());
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("######### - Error - #########");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return Flux.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el sistema", 500));
		}
	}
	
	@Override
	public Mono<Clients> created(ClientsDTO clientDTO) {
		LOGGER.info("######### - Se valida si el cliente con el email " + clientDTO.getEmail() + " existe - #########");
		if (findByEmail(clientDTO.getEmail()) != null) {
			LOGGER.warn("######### - No se puede crear el cliente - #########");
			LOGGER.warn("######### - Email de cliente ya se encuentra registrado - #########");
			return Mono.error(new CustomException(HttpStatus.CONFLICT, "Email de cliente ya se encuentra registrado", 409));
		}
		else {
			LOGGER.info("######### - Se inicia la creaión del cliente - #########");
			Clients client = mapDtoToEntity(clientDTO);
			client.setSharedKey(Utility.generatedAlias(client.getBussinessId()));
			client.setDataAdded(Utility.generateCurrentDate());
			return clientsRepo.save(client);
		}
	}
	
	@Override
	public Mono<Clients> modified(ClientsDTO clientDTO) {
		LOGGER.info("######### - Se valida si el cliente con el email " + clientDTO.getEmail() + " existe - #########");
		if (findByEmail(clientDTO.getEmail()) == null) {
			LOGGER.warn("######### - No se puede modificar el cliente - #########");
			LOGGER.warn("######### - Email de cliente no se encuentra registrado - #########");
			return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Email de cliente no se encuentra registrado", 404));
		}
		else {
			LOGGER.info("######### - Se actualiza el SharedKey del cliente - #########");
			clientDTO.setSharedKey(Utility.generatedAlias(clientDTO.getBussinessId()));
			LOGGER.info("######### - Se inicia la modificación del cliente - #########");
			return clientsRepo.save(mapDtoToEntity(clientDTO));
		}
	}
	
	@Override
	public Mono<Void> delete(long id) {
		LOGGER.info("######### - Se inicia la eliminación del cliente con id=" + id + " - #########");
		return clientsRepo.deleteById(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Mono<ByteArrayInputStream> export(List<ClientsDTO> clients) {
		String[] columns = {"sharedKey", "bussinessId", "email", "phone", "dataAdded"};
		return Mono.fromCallable(() -> {
            try {
                ByteArrayInOutStream stream = new ByteArrayInOutStream();
                OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
                CSVWriter writer = new CSVWriter(streamWriter);

                ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
                mappingStrategy.setType(ClientsDTO.class);
                mappingStrategy.setColumnMapping(columns);
                writer.writeNext(columns);

                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withMappingStrategy(mappingStrategy)
                        .withSeparator(',')
                        .build();

                beanToCsv.write(clients);
                streamWriter.flush();
                return stream.getInputStream();
            }
            catch (CsvException | IOException e) {
                throw new RuntimeException(e);
            }

        }).subscribeOn(Schedulers.boundedElastic());
	}
	
	
	
	// ---------------- Other Functions ------------------------\\
	
	
	public Clients findByEmail(String email) {
		try {
			LOGGER.info("######### - Se obtiene cliente con email=" + email + " - #########");
			return clientsRepo.findByEmail(email).toFuture().get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("######### - Error - #########");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public ClientsDTO mapEntityToDto(Clients client) {
		LOGGER.info("######### - Se convierte la Entity Client en DTO con Mapper - #########");
		return modelMapper.map(client, ClientsDTO.class);
	}
	
	public Clients mapDtoToEntity(ClientsDTO clientDto) {
		LOGGER.info("######### - Se convierte el DTO Client en Entity con Mapper - #########");
		return modelMapper.map(clientDto, Clients.class);
	}
	
	public ClientsDTO advancedSearchParams(AdvancedFilterDTO advancedFilterDTO,  Clients client) {
		LOGGER.info("######### - Se inician los filtros avanzados - #########");
		boolean flag1 = advancedFilterDTO.getName()!=null&&client.getBussinessId().contains(advancedFilterDTO.getName());
		boolean flag2 = advancedFilterDTO.getEmail()!=null&&client.getEmail().contains(advancedFilterDTO.getEmail());
		boolean flag3 = advancedFilterDTO.getPhone()!=null&&client.getPhone().contains(advancedFilterDTO.getPhone());
		boolean flag4 = advancedFilterDTO.getStartDate()!=null&&advancedFilterDTO.getEndDate()!=null&&
				Duration.between(advancedFilterDTO.getStartDate(), client.getDataAdded()).toDays() >= 0 &&
				Duration.between(client.getDataAdded(), advancedFilterDTO.getEndDate()).toDays() >= 0;
				LOGGER.info("######### - El resultado de los filtros avanzados es=" + (flag1 || flag2 || flag3 || flag4) + " - #########");
		return flag1 || flag2 || flag3 || flag4?mapEntityToDto(client):null;
	}
	
}
