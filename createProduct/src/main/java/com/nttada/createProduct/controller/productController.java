package com.nttada.createProduct.controller;

import java.util.HashMap;

import com.nttada.createProduct.entity.product;
import com.nttada.createProduct.repository.productRepository;
import com.nttada.createProduct.service.productService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("product")
@AllArgsConstructor
@RestController
public class productController {
	
	@Autowired
	private productService product_service;

    @Autowired
    private productRepository product_repo;



	@GetMapping("/all")
	public Flux<product> getAll() {
		System.out.println("::will returns ALL Students records::");
		return product_service.getAll();
	}
	
	
	@GetMapping("/getById/{id}")
	public Mono<product> getById(@PathVariable("id")  String id) {
		System.out.println("::will return a product record::");
		return product_service.getById(id);
	}

	@PutMapping("/update/{id}")
	public Mono<product> updateById(@PathVariable("id") String id, @RequestBody product client_doc) {
		return product_service.update( client_doc);
	}

	@PutMapping("/setInactive/{id}")
	public Mono<product> setInactive(@PathVariable("id") String id) {
		return product_repo.findById(id)
		.switchIfEmpty(Mono.error(new Exception("DOC_NOT_FOUND")))
		.map(update_doc  -> {			
			update_doc.setStatus("INACTIVE");
			return update_doc;
		})
		.flatMap(product_repo::save);
	}

	@PostMapping("/create/{id}")
	public Mono<product> save(@RequestBody  product product) {
		return product_service.save(product);
	}

	@DeleteMapping("/delete/{id}")
	public Mono<Void> delete(@PathVariable  String id) {
		return product_service.delete(id);
	}
}