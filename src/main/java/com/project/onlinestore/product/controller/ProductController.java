package com.project.onlinestore.product.controller;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.buyer.service.BuyerService;
import com.project.onlinestore.notification.service.NotificationService;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.product.service.ProductService;
import com.project.onlinestore.review.domain.Review;
import com.project.onlinestore.review.service.ReviewService;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.seller.service.SellerService;
import com.project.onlinestore.utils.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ReviewService reviewService;

    private ProductService productService;
    private BuyerService buyerService;
    private FileService fileService;
    private NotificationService notificationService;

    @ModelAttribute("link")
    private String getAdLink(HttpServletRequest request){
        String rootDirectory = request.getSession().getServletContext().getRealPath("/");
        return fileService.readFile(rootDirectory+"//link.txt");
    }

    @ModelAttribute("notification_number_buyer")
    private int getNotificationNumber(Principal principal){
        return notificationService.countUnseenNotifications(principal.getName());
    }

    @ModelAttribute("line_number")
    private int getLinesNumber(Principal principal){
        return buyerService.countLinesNumber(principal.getName());
    }

    @ModelAttribute("points")
    private Long getPoints(Principal principal){
        return buyerService.getBuyerByUsername(principal.getName()).getPoints();
    }

    @Autowired
    public ProductController(ProductService productService, FileService fileService,BuyerService buyerService, NotificationService notificationService) {
        this.productService = productService;
        this.fileService = fileService;
        this.buyerService = buyerService;
        this.notificationService = notificationService;
    }

//    anyone should have access to this link
    @GetMapping("/products/list")
    public String findAllProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "pages/products/list";
    }

//    anyone should have access to this link
    @GetMapping("/products/details/{id}")
    public String productDetails(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("follow",buyerService.isFollowed(buyerService.getBuyerByUsername(principal.getName()),product.getSeller()));
        List<Review> reviews = reviewService.getApprovedReviewsByProduct(id);
        model.addAttribute("average", getAverage(reviews));
        return "pages/products/details";
    }

    @GetMapping("/buyer/product/{id}/reviews")
    public String showReviews(@PathVariable Long id, Model model) {
        List<Review> reviews = reviewService.getApprovedReviewsByProduct(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("average", getAverage(reviews));
        List<Integer> rates = getRatingsNumber(reviews);
        model.addAttribute("ratings", rates);
        model.addAttribute("percentages", getRatingsPercentage(rates,reviews.size()));
        System.out.println(rates);
        return "pages/products/review";
    }

    private double getAverage(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRate();
        }
        return sum / reviews.size();
    }

    private List<Integer> getRatingsNumber(List<Review> reviews) {
        List<Integer> ratings = Arrays.asList(0, 0, 0, 0, 0);
        for (Review review : reviews) {
            int i = review.getRate() - 1;
            ratings.set(i,ratings.get(i) + 1);
        }
        return ratings;
    }

    private List<Integer> getRatingsPercentage(List<Integer> ratings, int size) {
        List<Integer> percentages = Arrays.asList(0, 0, 0, 0, 0);
        if (size == 0)
            return percentages;
        for (int i = 0; i < 5; i++) {
            percentages.set(i, calculatePercentage(ratings.get(i),size));
        }
        return percentages;
    }

    public int calculatePercentage(double obtained, double total) {
        double res = obtained * 100 / total;
        return (int) res;
    }

}
