package com.project.onlinestore.admin.controller;

import com.project.onlinestore.admin.domain.Ad;
import com.project.onlinestore.admin.service.AdminService;
import com.project.onlinestore.buyer.domain.Order;
import com.project.onlinestore.buyer.service.OrderService;
import com.project.onlinestore.notification.service.NotificationService;
import com.project.onlinestore.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    OrderService orderService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ReviewService reviewService;

    @ModelAttribute("notification_number_admin")
    private int getNotificationNumber(Principal principal){
        return notificationService.countUnseenNotifications(principal.getName());
    }

    @GetMapping("/notifications")
    public String getNotifications(Model model,Principal principal){
        model.addAttribute("notifications",notificationService.getUnseenNotifications(principal.getName()));
        notificationService.makeAllNotificationsSeenByUser(principal.getName());
        return "pages/admin/notifications";
    }

    @GetMapping("/pendingUsers")
    public String getPendingUsers(Model model){
        model.addAttribute("pendingSellers",adminService.getPendingSellers());
        return "pages/admin/pendingUsers";
    }

    @PostMapping("/accept/{id}")
    public String acceptSeller(@PathVariable Long id,RedirectAttributes redirectAttributes){
        adminService.acceptSeller(id);
        redirectAttributes.addFlashAttribute("successMessage", "Seller has been accepted!");
        return "redirect:/admin/pendingUsers";
    }

    @PostMapping("/reject/{id}")
    public String rejectSeller(@PathVariable Long id,RedirectAttributes redirectAttributes){
        adminService.rejectSeller(id);
        redirectAttributes.addFlashAttribute("successMessage", "Seller has been rejected!");
        return "redirect:/admin/pendingUsers";
    }

    @GetMapping("/adChange")
    public String home(@ModelAttribute Ad ad, Model model){
        return "pages/admin/adChange";
    }

    @PostMapping("/adChange")
    public String changeAd(@Valid @ModelAttribute("ad") Ad ad, BindingResult result, HttpServletRequest request,
                           RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "pages/admin/adChange";
        }
        MultipartFile productImage = ad.getImage();
        String rootDirectory = request.getSession().getServletContext().getRealPath("/");
        if (productImage != null && !productImage.isEmpty()){
            redirectAttributes.addFlashAttribute("successMessage", "Ad has been changed successfully!");
            adminService.changeAd(ad,rootDirectory);
        }
        return "redirect:/admin/adChange";
    }

    @GetMapping("/orders")
    public String getOrders(Model model){
        model.addAttribute("orders",orderService.findAll());
        model.addAttribute("status",0);
        return "pages/admin/orders";
    }

    @GetMapping("/orders/pending")
    public String getPendingOrders(Model model){
        model.addAttribute("orders",orderService.findAllByStatus(1));
        model.addAttribute("status",1);
        return "pages/admin/orders";
    }

    @GetMapping("/orders/shipped")
    public String getShippedOrders(Model model){
        model.addAttribute("orders",orderService.findAllByStatus(2));
        model.addAttribute("status",2);
        return "pages/admin/orders";
    }

    @GetMapping("/order/{id}")
    public String orderDetails(@PathVariable Long id,Model model){
        model.addAttribute("order",orderService.getOrderById(id));
        return "pages/orders/details";
    }

    @PostMapping("/orders/cancel")
    public String cancelOrder(Long id) {
        orderService.updateStatus(id,4);
        notificationService.notifyOrderBuyer(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/cancel/{id}")
    public String ShowCancelOrderForm(@PathVariable Optional<Long> id, Model model) {
        if(id.isPresent()) {
            Order order = orderService.findById(id.get());
            model.addAttribute("order", order);
        }
        return "pages/admin/cancelform";
    }

    @PostMapping("/orders/ship")
    public String shipOrder(Long id) {
        orderService.updateStatus(id,2);
        notificationService.notifyOrderBuyer(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/ship/{id}")
    public String ShowShipOrderForm(@PathVariable Optional<Long> id, Model model) {
        if(id.isPresent()) {
            Order order = orderService.findById(id.get());
            model.addAttribute("order", order);
        }
        return "pages/admin/shipform";
    }

    @PostMapping("/orders/deliver")
    public String deliverOrder(Long id) {
        orderService.updateStatus(id,3);
        notificationService.notifyOrderBuyer(id);
        notificationService.notifyOrderSeller(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/deliver/{id}")
    public String deliverShipOrderForm(@PathVariable Optional<Long> id, Model model) {
        if(id.isPresent()) {
            Order order = orderService.findById(id.get());
            model.addAttribute("order", order);
        }
        return "pages/admin/deliverform";
    }


    @GetMapping("/reviews")
    public String getPendingReviews(Model model){
        model.addAttribute("reviews",reviewService.getAllPendingReviews());
        return "pages/admin/pendingReviews";
    }

    @PostMapping("/reviews/accept/{id}")
    public String acceptReview(@PathVariable Long id,RedirectAttributes redirectAttributes){
        reviewService.acceptReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Review has been accepted!");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/reviews/reject/{id}")
    public String rejectReview(@PathVariable Long id,RedirectAttributes redirectAttributes){
        reviewService.rejectReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Review has been rejected!");
        return "redirect:/admin/reviews";
    }

}
