package com.nttdata.createProduct.service.Impl;

import java.util.List;

import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.repository.ProductRepository;
import com.nttdata.createProduct.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> getAll() {
      return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product){
      return productRepository.save(product);
    }
}
