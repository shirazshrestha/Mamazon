package com.project.onlinestore.security.controller;

import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {

        @Autowired
        UserService userService;
        @RequestMapping("/default")
        public String defaultAfterLogin() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            if (user.getRole().getId()== SecurityConstants.ROLE_ADMIN)
                return "redirect:/admin/pendingUsers";
            if (user.getRole().getId()== SecurityConstants.ROLE_BUYER)
                return "redirect:/products/list";
            if (user.getRole().getId()== SecurityConstants.ROLE_SELLER)
                return "redirect:/seller/myproducts";
            return "redirect:/login";
        }
}
