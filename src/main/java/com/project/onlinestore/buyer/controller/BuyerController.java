package com.project.onlinestore.buyer.controller;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.domain.Order;
import com.project.onlinestore.buyer.service.BuyerService;
import com.project.onlinestore.buyer.service.OrderService;
import com.project.onlinestore.notification.service.NotificationService;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.product.service.ProductService;
import com.project.onlinestore.review.domain.Review;
import com.project.onlinestore.review.repository.ReviewRespository;
import com.project.onlinestore.review.service.ReviewService;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.utils.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    FileService fileService;

    @Autowired
    BuyerService buyerService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;


    @ModelAttribute("link")
    private String getAdLink(HttpServletRequest request){
        String rootDirectory = request.getSession().getServletContext().getRealPath("/");
        return fileService.readFile(rootDirectory+"//link.txt");
    }

    @ModelAttribute("notification_number_buyer")
    private int getNotificationNumber(Principal principal){
        return notificationService.countUnseenNotifications(principal.getName());
    }

    @ModelAttribute("points")
    private Long getPoints(Principal principal){
        return buyerService.getBuyerByUsername(principal.getName()).getPoints();
    }

    @ModelAttribute("line_number")
    private int getLinesNumber(Principal principal){
        return buyerService.countLinesNumber(principal.getName());
    }



    @GetMapping("/")
    public String home(){
        return "redirect:/products/list";
    }

    @GetMapping("/follow/{id}")
    public @ResponseBody void follow(@PathVariable Long id, Principal principal){
        buyerService.follow(principal.getName(),id);
    }

    @GetMapping("/unfollow/{id}")
    public @ResponseBody void unfollow(@PathVariable Long id, Principal principal){
        buyerService.unfollow(principal.getName(),id);
    }

    @GetMapping("/notifications")
    public String getNotifications(Model model,Principal principal){
        model.addAttribute("notifications",notificationService.getUnseenNotifications(principal.getName()));
        notificationService.makeAllNotificationsSeenByUser(principal.getName());
        return "pages/buyer/notifications";
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id,Model model){
        model.addAttribute("order",orderService.getOrderById(id));
        return "pages/orders/details";
    }

    @GetMapping("/myorders")
    public String buyerOrders(Model model, Principal principal) {
        Buyer buyer = buyerService.getBuyerByUsername(principal.getName());
        model.addAttribute("orders", orderService.findAllByBuyer(buyer));
        model.addAttribute("status", 0);
        return "pages/buyer/myorders";
    }



    @PostMapping("/orders/cancel")
    public String cancelOrder(Long id) {
        Order order = orderService.getOrderById(id);
        order.setStatus(4);
        orderService.save(order);
        Buyer buyer = order.getBuyer();
        buyer.setPoints(buyer.getPoints()-100);
        buyerService.save(buyer);
        notificationService.notifyOrderAdmin(id);
        return "redirect:/buyer/myorders";
    }

    @GetMapping("/orders/cancelform/{id}")
    public String ShowDeleteProductForm(@PathVariable Optional<Long> id, Model model) {
        if(id.isPresent()) {
            Order order = orderService.findById(id.get());
            model.addAttribute("order", order);
        }
        return "pages/orders/cancelform";
    }

    @GetMapping("/pendingorders")
    public String buyerPendingOrders(Model model, Principal principal) {
        Buyer buyer = buyerService.getBuyerByUsername(principal.getName());
        model.addAttribute("orders", orderService.findAllByBuyerAndStatusEquals(buyer, 1));
        model.addAttribute("status", 1);
        return "pages/buyer/myorders";
    }

    @GetMapping("/review/{id}")
    public String getReviewForm(@ModelAttribute("review") Review review,@PathVariable Long id, Model model){
        model.addAttribute("productId",id);
        return "pages/reviews/reviewform";
    }

    @PostMapping("/review/{id}")
    public String reviewProduct(@Valid @ModelAttribute("review") Review review, BindingResult result,@PathVariable Long id,Principal principal){
        if (result.hasErrors()){
            return "pages/reviews/reviewform";
        }
        review.setProduct(productService.findById(id));
        review.setBuyer(buyerService.getBuyerByUsername(principal.getName()));
        review.setLocalDateTime(LocalDateTime.now());
        reviewService.save(review);
        notificationService.notifyAdminReview(principal.getName(),id);
        return "redirect:/buyer/myorders";
    }

    @GetMapping("/reviews")
    public String getProductsToReview(Model model,Principal principal){
        model.addAttribute("products",reviewService.getProductsNotReviewed(principal.getName()));
        return "pages/buyer/productsToReview";
    }

}
