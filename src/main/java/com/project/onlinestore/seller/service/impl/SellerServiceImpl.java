package com.project.onlinestore.seller.service.impl;

import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.seller.repository.SellerRepository;
import com.project.onlinestore.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public Seller getSellerByUser(User user) {
        return sellerRepository.findSellerByUser(user);
    }

    @Override
    public Seller save(Seller seller) {
        return sellerRepository.save(seller);
    }
}
