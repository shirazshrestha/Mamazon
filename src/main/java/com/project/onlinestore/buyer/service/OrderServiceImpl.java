package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.buyer.domain.Line;
import com.project.onlinestore.buyer.domain.Order;
import com.project.onlinestore.buyer.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Autowired
    BuyerService buyerService;

    @Override
    public Order saveOrder(Order order, Long cartId,String username) {
        order.setLocalDateTime(LocalDateTime.now());
        Cart cart = cartService.getById(cartId);
        order.setLines(getCopyOfLines(cart.getLines()));
        order.setBuyer(buyerService.getBuyerByUsername(username));
        Order newOrder = orderRepository.save(order);
        buyerService.removeCartAndUpdatePoints(username,order.getPointUsed());
        return newOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public List<Order> findAllByBuyer(Buyer buyer) {
        return orderRepository.findAllByBuyerOrderByLocalDateTimeDesc(buyer);
    }

    @Override
    public List<Order> findAllByBuyerAndStatusEquals(Buyer buyer, int status) {
        return this.orderRepository.findAllByBuyerAndStatusEqualsOrderByLocalDateTimeDesc(buyer, status);
    }

    @Override
    public void save(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByLocalDateTimeDesc();
    }

    @Override
    public List<Order> findAllByStatus(int status) {
        return orderRepository.findAllByStatusEqualsOrderByLocalDateTimeDesc(status);
    }

    @Override
    public void updateStatus(Long orderId, int status) {
        Order order = findById(orderId);
        order.setStatus(status);
        save(order);
    }


    private List<Line> getCopyOfLines(List<Line> lines){
        List<Line> newLines = new ArrayList<>();

        for (Line line:lines){
            Line newLine = new Line();
            newLine.setProduct(line.getProduct());
            newLine.setQty(line.getQty());
            newLines.add(newLine);
        }

        return newLines;
    }
}
