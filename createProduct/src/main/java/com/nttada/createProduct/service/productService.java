package com.nttada.createProduct.service;

import com.nttada.createProduct.entity.product;
import com.nttada.createProduct.repository.productRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@AllArgsConstructor
public class productService {
    @Autowired
	private productRepository product_repo;


	public Flux<product> getAll() {
		return product_repo.findAll().switchIfEmpty(Flux.empty());
	}

    public Flux<product> findByProductTypeAndClientId(String ProductType, String clientId) {
		return product_repo.findByProductTypeAndClientId(ProductType,clientId ).switchIfEmpty(Flux.empty());
	}

	public Mono<product> getById( String id) {
		return product_repo.findById(id);
	}

	public Mono <product> update( product client_doc) {
		return product_repo.save(client_doc);
	}

	public Mono <product> save( product client_doc) {
		return product_repo.save(client_doc);
	}
	
	public Mono <Void> delete( String id) {		
        return product_repo.deleteById(id);
    }
		
}
