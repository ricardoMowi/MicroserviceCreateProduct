package com.nttada.createProduct.repository;

import com.nttada.createProduct.entity.client;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface clientRepository extends ReactiveMongoRepository <client, String>{
    
}
