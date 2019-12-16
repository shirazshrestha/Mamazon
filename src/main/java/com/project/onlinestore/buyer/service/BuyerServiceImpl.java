package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.buyer.repository.BuyerReposiotry;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.seller.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    BuyerReposiotry buyerReposiotry;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserService userService;

    @Override
    public Buyer save(Buyer buyer) {
        return buyerReposiotry.save(buyer);
    }

    @Override
    public Buyer getBuyerByUsername(String username) {
        User user = userService.findUserByUserName(username);
        return buyerReposiotry.findBuyerByUser(user);
    }

    @Override
    public Boolean follow(String buyername, Long sellerId) {
        Buyer buyer = getBuyerByUsername(buyername);
        if (buyer.getFollowing()==null)
            buyer.setFollowing(new ArrayList<>());
        buyer.getFollowing().add(sellerRepository.findById(sellerId).get());
        buyerReposiotry.save(buyer);
        return true;
    }

    @Override
    public Boolean unfollow(String buyername, Long sellerId) {
        Buyer buyer = getBuyerByUsername(buyername);
        if (buyer.getFollowing()==null)
            buyer.setFollowing(new ArrayList<>());
        buyer.getFollowing().remove(sellerRepository.findById(sellerId).get());
        buyerReposiotry.save(buyer);
        return true;
    }

    @Override
    public boolean isFollowed(Buyer buyer, Seller seller) {
        if (buyer.getFollowing()==null)
            return false;
        return buyer.getFollowing().contains(seller);
    }

    @Override
    public int countLinesNumber(String username) {
        return getBuyerByUsername(username).getCart().getLines().size();
    }

    @Override
    public void removeCartAndUpdatePoints(String username,Long pointsUsed) {
        Buyer buyer= getBuyerByUsername(username);
        buyer.setPoints(buyer.getPoints()+100-pointsUsed);
        buyer.setCart(new Cart());
        save(buyer);
    }


}
