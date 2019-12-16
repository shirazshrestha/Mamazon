package com.project.onlinestore.product.repository;

import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.seller.domain.Seller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findProductsBySeller(Seller seller);
}
