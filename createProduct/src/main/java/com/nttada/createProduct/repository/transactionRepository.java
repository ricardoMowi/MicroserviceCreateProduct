package com.nttada.createProduct.repository;

import org.springframework.stereotype.Repository;

import com.nttada.createProduct.entity.transaction;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

@Repository
public interface transactionRepository extends ReactiveMongoRepository <transaction, String>{
    
}
