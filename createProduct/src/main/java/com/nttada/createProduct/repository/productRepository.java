package com.nttada.createProduct.repository;

import java.util.List;

import com.nttada.createProduct.entity.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface productRepository extends ReactiveMongoRepository <product, String> {
    List<product> findByClientId(String clientId);
    List<product> findByProductTypeAndStatus(String ProductType, String Status);
    Flux<product> findByProductTypeAndClientId (String ProductType, String clientId);   
}
