package com.project.onlinestore.buyer.repository;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.security.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerReposiotry extends CrudRepository<Buyer, Long> {
    Buyer findBuyerByUser(User user);
}
