package com.project.onlinestore.buyer.repository;

import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.buyer.domain.Line;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {

}
