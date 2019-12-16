package com.project.onlinestore.review.repository;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.review.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRespository extends CrudRepository<Review,Long> {
    List<Review> findByProductAndStatusEqualsOrderByLocalDateTimeDesc(Product product,int status);
    boolean existsByProductAndBuyer(Product product, Buyer buyer);
    List<Review> findAllByStatusEquals(int status);
}
