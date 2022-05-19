package com.nttada.createProduct.service;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nttada.createProduct.entity.client;
import com.nttada.createProduct.entity.product;
import com.nttada.createProduct.repository.clientRepository;
import com.nttada.createProduct.repository.productRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@AllArgsConstructor

public class clientService {
    @Autowired
	private clientRepository client_repo;

	@Autowired
	private productRepository product_repo;

	public Flux<client> getAll() {
		return client_repo.findAll().switchIfEmpty(Flux.empty());
	}

	public Flux<product> find1(String ProductType, String clientId) {
		return product_repo.findByProductTypeAndClientId( ProductType,  clientId).switchIfEmpty(Flux.empty());
	}

	public Mono<client> getById( String id) {
		return client_repo.findById(id);
	}

	public Mono <client> update( client client_doc) {
		return client_repo.save(client_doc);
	}

	public Mono <client> save( client client_doc) {
		return client_repo.save(client_doc);
	}
	
	public Mono <Void> delete( String id) {		
		return client_repo.deleteById(id);
	}
}
