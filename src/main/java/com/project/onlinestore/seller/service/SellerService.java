package com.project.onlinestore.seller.service;

import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.seller.domain.Seller;

public interface SellerService {
    Seller getSellerByUser(User user);
    Seller save(Seller seller);
}
