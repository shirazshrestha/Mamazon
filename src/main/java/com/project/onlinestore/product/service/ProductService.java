package com.project.onlinestore.product.service;

import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.seller.domain.Seller;

import java.util.List;

public interface ProductService {
    Product save(Product product);
    List<Product> findAll();
    void delete(Product product);
    Product findById(Long id);
    List<Product> findProductsBySeller(Seller seller);
}
