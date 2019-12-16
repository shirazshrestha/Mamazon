package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.seller.domain.Seller;

public interface BuyerService {
    Buyer save(Buyer buyer);
    Buyer getBuyerByUsername(String username);
    Boolean follow(String buyername,Long sellerId);
    Boolean unfollow(String buyername,Long sellerId);
    boolean isFollowed(Buyer buyer, Seller seller);
    int countLinesNumber(String username);
    void removeCartAndUpdatePoints(String username,Long pointsUsed);
}
