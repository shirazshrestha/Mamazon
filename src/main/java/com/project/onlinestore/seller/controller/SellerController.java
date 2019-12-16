package com.project.onlinestore.seller.controller;

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
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ReviewService reviewService;

    private ProductService productService;
    private SellerService sellerService;
    private UserService userService;
    private FileService fileService;

    @Autowired
    public SellerController(ProductService productService, SellerService sellerServicel, UserService userService, FileService fileService) {
        this.productService = productService;
        this.sellerService = sellerServicel;
        this.userService = userService;
        this.fileService = fileService;
    }

    @ModelAttribute("notification_number_seller")
    private int getNotificationNumber(Principal principal) {
        return notificationService.countUnseenNotifications(principal.getName());
    }

    @GetMapping("/notifications")
    public String getNotifications(Model model, Principal principal) {
        model.addAttribute("notifications", notificationService.getUnseenNotifications(principal.getName()));
        notificationService.makeAllNotificationsSeenByUser(principal.getName());
        return "pages/seller/notifications";
    }

    @GetMapping("/myproducts")
    public String listSellerProducts(Model model, Principal principal) {
        User user = userService.findUserByUserName(principal.getName());
        Seller seller = sellerService.getSellerByUser(user);
        model.addAttribute("products", productService.findProductsBySeller(seller));
        return "pages/seller/products/list";
    }

    @GetMapping({"/products/add", "/products/edit/{id}"})
    public String showAddProductForm(@ModelAttribute Product product, @PathVariable Optional<Long> id, Model model) {
        if (id.isPresent()) {
            product = productService.findById(id.get());
            model.addAttribute("product", product);
            System.out.println("yes");
        }
        return "pages/products/add-form";
    }


    @PostMapping({"/products/add"})
    public String addProduct(@Valid Product product, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Principal principal, HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            return "pages/products/add-form";
        }
        User user = userService.findUserByUserName(principal.getName());
        product.setSeller(sellerService.getSellerByUser(user));
        Product newProduct = productService.save(product);
        if (product.getImage() != null) {
            MultipartFile productImage = product.getImage();
            String rootDirectory = request.getSession().getServletContext().getRealPath("/");
            if (productImage != null && !productImage.isEmpty())
                fileService.transferImage(rootDirectory + "//" + newProduct.getId(), productImage, "png");
        }
        redirectAttributes.addFlashAttribute("created", "Product was created successfully");
        return "redirect:/seller/myproducts";
    }

    @GetMapping("/products/delete/{id}")
    public String ShowDeleteProductForm(@PathVariable Optional<Long> id, Model model) {
        if (id.isPresent()) {
            Product product = productService.findById(id.get());
            model.addAttribute("product", product);
        }
        return "pages/products/delete-form";
    }


    @PostMapping("/products/delete")
    public String DeleteProduct(Product product, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("deleted", "Product was deleted successfully!");
        this.productService.delete(product);
        return "redirect:/seller/myproducts";
    }

    @GetMapping("/product/{id}/reviews")
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
