package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.buyer.domain.Line;
import com.project.onlinestore.buyer.repository.CartRepository;
import com.project.onlinestore.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BuyerService buyerService;

    @Autowired
    LineService lineService;

    public Cart addToCart(Long productId, String username, int quantity) {
        Line line = new Line();
        line.setQty(quantity);
        line.setProduct(productService.findById(productId));

        Cart cart= buyerService.getBuyerByUsername(username).getCart();
        if (cart==null)
            cart = new Cart();
        cart.addLine(line);

        return cartRepository.save(cart);
    }

    @Override
    public void removeLine(String username, Long lineId) {
        Cart cart= buyerService.getBuyerByUsername(username).getCart();
        cart.getLines().remove(lineService.getLineById(lineId));
        saveCart(cart);
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public Cart getById(Long cartId) {
        return cartRepository.findById(cartId).get();
    }

    @Override
    public void updateLine(String username, Long lineId, Integer qty) {
        Cart cart= buyerService.getBuyerByUsername(username).getCart();
        int index = cart.getLines().indexOf(lineService.getLineById(lineId));
        cart.getLines().get(index).setQty(qty);
        cartRepository.save(cart);
    }
}
