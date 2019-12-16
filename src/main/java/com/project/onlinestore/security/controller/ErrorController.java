package com.project.onlinestore.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/access-denied")
    public String accessDenied(){
        return "pages/errors/403";
    }
}