package com.nttdata.createProduct.repository;

import com.nttdata.createProduct.entity.Client;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository  extends MongoRepository <Client, String> {
    
}
