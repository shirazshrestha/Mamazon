package com.project.onlinestore.security.controller;

import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.RoleService;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/registration")
    public String registration(@ModelAttribute User user){
        return "pages/security/registration";
    }

    @PostMapping("/registration")
    public String saveUser(@Valid User user, BindingResult result, Model model){
        User userExists = userService.findUserByUserName(user.getUsername());
        if (userExists != null) {
            result
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided");

        }
        if (result.hasErrors()) {
            return "pages/security/registration";
        }
        user.setRole((roleService.getById(user.getRole().getId())));
        userService.saveUser(user);
        if (user.getRole().getId()== SecurityConstants.ROLE_SELLER)
            model.addAttribute("successMessage", "Seller has been registered successfully! Please wait for admin approval");
        else if (user.getRole().getId()== SecurityConstants.ROLE_BUYER)
            model.addAttribute("successMessage", "Buyer has been registered successfully");
        model.addAttribute("user", new User());
        return "pages/security/registration";
    }
}