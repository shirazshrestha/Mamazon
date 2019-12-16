package com.project.onlinestore.buyer.controller;

import com.project.onlinestore.buyer.domain.Address;
import com.project.onlinestore.buyer.domain.Cart;
import com.project.onlinestore.buyer.domain.Order;
import com.project.onlinestore.buyer.service.BuyerService;
import com.project.onlinestore.buyer.service.CartService;
import com.project.onlinestore.buyer.service.OrderService;
import com.project.onlinestore.notification.service.NotificationService;
import com.project.onlinestore.utils.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    FileService fileService;

    @Autowired
    BuyerService buyerService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    OrderService orderService;

    @ModelAttribute("link")
    private String getAdLink(HttpServletRequest request) {
        String rootDirectory = request.getSession().getServletContext().getRealPath("/");
        return fileService.readFile(rootDirectory + "//link.txt");
    }

    @ModelAttribute("notification_number_buyer")
    private int getNotificationNumber(Principal principal) {
        return notificationService.countUnseenNotifications(principal.getName());
    }

    @ModelAttribute("line_number")
    private int getLinesNumber(Principal principal) {
        return buyerService.countLinesNumber(principal.getName());
    }

    @ModelAttribute("points")
    private Long getPoints(Principal principal){
        return buyerService.getBuyerByUsername(principal.getName()).getPoints();
    }

    @ModelAttribute("cart")
    private Cart getCart(Principal principal) {
        return buyerService.getBuyerByUsername(principal.getName()).getCart();
    }

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long productId, @RequestParam(value = "qty", required = false, defaultValue = "1") Integer qty, Principal principal) {
        String username = principal.getName();
        cartService.addToCart(productId, username, qty);
        return "redirect:/buyer/cart";
    }

    @GetMapping("/buyer/cart")
    public String showCart() {
        return "/pages/buyer/cart";
    }

    @PostMapping("/buyer/cart/delete")
    public String deleteLine(Long line_id, Principal principal) {
        cartService.removeLine(principal.getName(), line_id);
        return "redirect:/buyer/cart";
    }

    @GetMapping("/cart/checkout")
    public String checkout(@ModelAttribute Order order, Principal principal) {
        if (getLinesNumber(principal) == 0)
            throw new RuntimeException("You cannot purchase with an empty cart");
        return "pages/buyer/checkout";
    }

    @PostMapping("/cart/purchase")
    public String saveOrder(@Valid @ModelAttribute("order") Order order, BindingResult result, @RequestParam(value = "sameadr", required = false) String sameAddress,Principal principal) {
        if (sameAddress != null) {
            Address billingAddress = new Address();
            billingAddress.setName(order.getShippingAddress().getName());
            billingAddress.setStreet(order.getShippingAddress().getStreet());
            billingAddress.setState(order.getShippingAddress().getState());
            billingAddress.setZipCode(order.getShippingAddress().getZipCode());
            order.setBillingAddress(billingAddress);
            result = ignoreFieldsContaining("billingAddress",result,order);
        }

        if (result.hasErrors()) {
            return "pages/buyer/checkout";
        }

        Order newOrder=orderService.saveOrder(order,getCart(principal).getId(),principal.getName());
        notificationService.notifyOrderAdmin(newOrder.getId());

        return "redirect:/buyer/orders/"+newOrder.getId();
    }

    private BindingResult ignoreFieldsContaining(String ignoreString, BindingResult result,Order order){
        List<FieldError> errorsToKeep = result.getFieldErrors().stream()
                .filter(fer -> !fer.getField().contains(ignoreString))
                .collect(Collectors.toList());

        BindingResult newResult = new BeanPropertyBindingResult(order, "order");

        for (FieldError fieldError : errorsToKeep) {
            newResult.addError(fieldError);
        }

        return  newResult;
    }

    @GetMapping("/buyer/updateline/{id}/{qty}")
    @ResponseBody
    private void updateQty(@PathVariable Long id, @PathVariable int qty,Principal principal) {
        cartService.updateLine(principal.getName(),id,qty);
    }
}
