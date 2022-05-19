package com.nttada.createProduct.controller;


    



import java.util.HashMap;
import java.util.Optional;

import com.nttada.createProduct.entity.client;
import com.nttada.createProduct.repository.clientRepository;
import com.nttada.createProduct.service.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.net.server.Client;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("client")
@AllArgsConstructor
@RestController
public class clientController {
	
	@Autowired
	private clientService client_service;

	@Autowired
	private clientRepository client_repo;

	@GetMapping("/all")
	public Flux<client> getAll() {
		System.out.println("::will returns ALL Students records::");
		return client_service.getAll();
	}
	
	
	@GetMapping("/getById/{id}")
	public Mono<client> getById(@PathVariable("id")  String id) {
		System.out.println("::will return a client record::");
		return client_service.getById(id);
	}

	@PutMapping("/update/{id}")
	public Mono<client> updateById(@PathVariable("id") String id, @RequestBody client client_doc) {
		return client_service.update( client_doc);
	}

	@PutMapping("/setInactive/{id}")
	public Mono<client> setInactive(@PathVariable("id") String id) {
		return client_repo.findById(id)
		.switchIfEmpty(Mono.error(new Exception("DOC_NOT_FOUND")))
		.map(update_doc  -> {			
			update_doc.setStatus("INACTIVE");
			return update_doc;
		})
		.flatMap(client_repo::save);
	}

	@PostMapping("/create/{id}")
	public Mono<client> save(@RequestBody  client client) {
		return client_service.save(client);
	}

	//Clase interna para validar cliente y tarjeta de credito
	public HashMap<String, Object> validateClient(String id) {    
		
		//Para solicitar este producto el cliente debe tener una tarjeta de crédito con el banco al momento de la creación de la cuenta
		HashMap<String, Object> map = new HashMap<>();
		//Mono<client> client_doc = client_repo.findById(id);





		// if (client_doc.) {

		// 	Client current_cli = Client.class.cast(client_doc.get());
			
		// 	//Existe, obtener cantidad de cuentas
		// 	List <Product> Products_1 = productRepo.findByProductTypeAndClientId("SAVING_ACCOUNT",id);  
		// 	List <Product> Products_2 = productRepo.findByProductTypeAndClientId("CURRENT_ACCOUNT",id);  
		// 	List <Product> Products_3 = productRepo.findByProductTypeAndClientId("FIXED_TERM_ACCOUNT",id);  
		// 	int Q_1 =  Products_1.size();
		// 	int Q_2 =  Products_2.size();
		// 	int Q_3 =  Products_3.size();
			
		// 	//Armar hashmap
		// 	map.put("message", "Id de cliente encontrado");
		// 	map.put("IdClient", id);
		// 	map.put("ClientType", current_cli.getClientType());
		// 	map.put("cant_cuenta_ahorro", Q_1);
		// 	map.put("cant_cuenta_corriente", Q_2);
		// 	map.put("cant_cuenta_plazo_fijo", Q_3);
		// }else{
		// 	map.put("message", "Id de cliente no encontrado");
		// }
		// return map;
	}

	@PostMapping("/createPersonVIP/{id}")
	public Mono<client> createPersonVIP(@RequestBody  client new_client) {
		return client_repo.findById(new_client.getId())
		.switchIfEmpty(Mono.error(new Exception("DOC_NOT_FOUND")))
		.map(update_doc  -> {			
			update_doc.setStatus("INACTIVE");
			return update_doc;
		})
		.flatMap(client_repo::save);
		return client_service.save(new_client);
	}

	@DeleteMapping("/delete/{id}")
	public Mono<Void> delete(@PathVariable  String id) {
		return client_service.delete(id);
	}
}