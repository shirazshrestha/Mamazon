package com.project.onlinestore.security.controller;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleError(HttpServletRequest req,
                                    MaxUploadSizeExceededException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg", "Image exceeds its maximum permitted size of 1MB");
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("pages/errors/exception");
        return mav;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleError(HttpServletRequest req,
                                    HttpRequestMethodNotSupportedException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg", "This request cannot be handled");
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("pages/errors/exception");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req,
                                    Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg", exception.getMessage());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("pages/errors/exception");
        return mav;
    }
}
