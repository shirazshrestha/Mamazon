package com.project.onlinestore.security.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.buyer.service.BuyerService;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.respository.UserRepository;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.seller.service.SellerService;
import com.project.onlinestore.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BuyerService buyerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        User newUser=null;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole().getId()== SecurityConstants.ROLE_BUYER) {
            user.setActive(1);
            newUser = userRepository.save(user);
            Buyer buyer = new Buyer();
            buyer.setUser(newUser);
            buyer.setCart(new Cart());
            buyerService.save(buyer);
        }
        else if (user.getRole().getId()== SecurityConstants.ROLE_SELLER){
            user.setActive(0);
            newUser = userRepository.save(user);
        }
        return newUser;
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
