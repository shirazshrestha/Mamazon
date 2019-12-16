package com.project.onlinestore.buyer.repository;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order,Long> {
    List<Order> findAllByBuyerOrderByLocalDateTimeDesc(Buyer buyer);
    List<Order> findAllByBuyerAndStatusEqualsOrderByLocalDateTimeDesc(Buyer buyer, int status);
    List<Order> findAllByOrderByLocalDateTimeDesc();
    List<Order> findAllByStatusEqualsOrderByLocalDateTimeDesc(int status);
}
