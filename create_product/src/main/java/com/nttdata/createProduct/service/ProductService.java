package com.nttdata.createProduct.service;

import java.util.List;

import com.nttdata.createProduct.entity.Product;

public interface ProductService {
    List<Product> getAll();
    Product createProduct(Product new_product);
}
