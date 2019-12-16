package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order,Long cartId,String username);
    Order getOrderById(Long id);
    List<Order> findAllByBuyer(Buyer buyer);
    List<Order> findAllByBuyerAndStatusEquals(Buyer buyer, int status);
    void save(Order order);
    Order findById(Long id);
    List<Order> findAll();
    List<Order> findAllByStatus(int status);
    void updateStatus(Long orderId,int status);
}
