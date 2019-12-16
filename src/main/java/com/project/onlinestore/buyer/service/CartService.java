package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Cart;


public interface CartService {
    Cart addToCart(Long productId, String username, int quantity);
    void removeLine(String username,Long lineId);
    void saveCart(Cart cart);
    Cart getById(Long cartId);
    void updateLine(String username,Long lineId,Integer qty);
}
