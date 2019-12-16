package com.project.onlinestore.review.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Line;
import com.project.onlinestore.buyer.domain.Order;
import com.project.onlinestore.buyer.service.BuyerService;
import com.project.onlinestore.buyer.service.OrderService;
import com.project.onlinestore.email.EmailService;
import com.project.onlinestore.notification.service.NotificationService;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.product.service.ProductService;
import com.project.onlinestore.review.domain.Review;
import com.project.onlinestore.review.repository.ReviewRespository;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRespository reviewRespository;

    @Autowired
    ProductService productService;

    @Autowired
    BuyerService buyerService;

    @Autowired
    OrderService orderService;

    @Autowired
    EmailService emailService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @Override
    public Review save(Review review) {
        return reviewRespository.save(review);
    }

    @Override
    public List<Review> getApprovedReviewsByProduct(Long productId) {
        return reviewRespository.findByProductAndStatusEqualsOrderByLocalDateTimeDesc(productService.findById(productId), 2);
    }

    @Override
    public List<Product> getProductsNotReviewed(String username) {
        Buyer buyer = buyerService.getBuyerByUsername(username);
        List<Order> orders = orderService.findAllByBuyerAndStatusEquals(buyer, 3);
        return orders.stream().map(Order::getLines).flatMap(Collection::stream)
                .map(Line::getProduct).distinct().filter(p-> !reviewRespository.existsByProductAndBuyer(p,buyer)).collect(Collectors.toList());
    }

    @Override
    public List<Review> getAllPendingReviews() {
        return reviewRespository.findAllByStatusEquals(1);
    }

    @Override
    public void acceptReview(Long id) {
        Review review = reviewRespository.findById(id).get();
        review.setStatus(2);
        reviewRespository.save(review);
        String username = review.getBuyer().getUser().getUsername();
        emailService.sendPendingAcceptanceReview(userService.findUserByUserName(username),review);
        notificationService.notifyBuyerReviewAccepted(username,review.getProduct().getId());
        notificationService.notifySellerReview(review.getProduct().getSeller().getUser().getUsername(),review.getProduct().getId());
    }

    @Override
    public void rejectReview(Long id) {
        Review review = reviewRespository.findById(id).get();
        reviewRespository.delete(review);

        String username = review.getBuyer().getUser().getUsername();
        emailService.sendPendingRejectReview(userService.findUserByUserName(username),review);
        notificationService.notifyBuyerReviewRejected(username,review.getProduct().getId());
    }


}
