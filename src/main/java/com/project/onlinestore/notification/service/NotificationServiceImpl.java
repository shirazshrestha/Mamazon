package com.project.onlinestore.notification.service;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Line;
import com.project.onlinestore.buyer.domain.Order;
import com.project.onlinestore.buyer.service.BuyerService;
import com.project.onlinestore.buyer.service.OrderService;
import com.project.onlinestore.notification.domain.Notification;
import com.project.onlinestore.notification.repository.NotificationRepository;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.seller.domain.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Override
    public Notification save(User user,String message,String link) {
        Notification notification = new Notification();
        notification.setLink(link);
        notification.setLocalDateTime(LocalDateTime.now());
        notification.setMessage(message);
        notification.setUser(user);
        return notificationRepository.save(notification);
    }

    @Override
    public int countUnseenNotifications(String username) {
        return notificationRepository.countAllByUserAndSeenIsFalse(userService.findUserByUserName(username));
    }

    @Override
    public List<Notification> getUnseenNotifications(String username) {
        return notificationRepository.findByUserOrderByLocalDateTimeDesc(userService.findUserByUserName(username));
    }

    @Override
    public void notifyAllFollowers(Seller seller, String message,String link) {
        List<Buyer> buyers = seller.getFollowers();
        for (Buyer buyer:buyers){
            User user = buyer.getUser();
            save(user,message,link);
        }
    }

    @Override
    public void makeAllNotificationsSeenByUser(String username) {
        List<Notification> notifications = notificationRepository.findByUser(userService.findUserByUserName(username));
        for (Notification notification:notifications){
            notification.setSeen(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void notifyOrderBuyer(Long orderId) {
        Order order = orderService.findById(orderId);
        User user = order.getBuyer().getUser();
        String link = "http://localhost:8080/buyer/myorders";
        String message = "Order #"+order.getId()+" is "+order.getStatusText();
        save(user,message,link);
    }

    @Override
    public void notifyOrderAdmin(Long orderId) {
        Order order = orderService.findById(orderId);
        User user = userService.findUserByUserName("admin");
        String link = "http://localhost:8080/admin/orders";
        String message = "Order #"+order.getId()+" is "+order.getStatusText();
        save(user,message,link);
    }

    @Override
    public void notifyOrderSeller(Long orderId) {
        Order order = orderService.findById(orderId);
        for (Line line:order.getLines()) {
            User user = line.getProduct().getSeller().getUser();
            String link = "http://localhost:8080/seller/myproducts";
            String message = "Product #" + line.getProduct().getId()+" "+line.getProduct().getTitle() + " has been bought " + line.getQty()+" times";
            save(user, message, link);
        }
    }

    @Override
    public void notifyAdminReview(String username,Long productId) {
        User user = userService.findUserByUserName("admin");
        String link = "http://localhost:8080/admin/reviews";
        String message = "Product #"+productId+" has been reviewed by user "+username;
        save(user,message,link);
    }

    @Override
    public void notifyBuyerReviewAccepted(String username, Long productId) {
        User user = userService.findUserByUserName(username);
        String link = "http://localhost:8080/buyer/product/"+productId+"/reviews";
        String message = "Your review for Product #"+productId+" has been approved";
        save(user,message,link);
    }

    @Override
    public void notifyBuyerReviewRejected(String username, Long productId) {
        User user = userService.findUserByUserName(username);
        String link = "http://localhost:8080/buyer/reviews";
        String message = "Your review for Product #"+productId+" has been rejected";
        save(user,message,link);
    }

    @Override
    public void notifySellerReview(String username, Long productId) {
        User user = userService.findUserByUserName(username);
        String link = "http://localhost:8080/seller/product/"+productId+"/reviews";
        String message = "Your Product #"+productId+" has a new review";
        save(user,message,link);
    }
}
