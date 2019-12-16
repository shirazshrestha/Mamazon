package com.project.onlinestore.utils.controller;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.product.service.ProductService;
import com.project.onlinestore.utils.GeneratePdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

@Controller
public class PdfExcelController {

    @Autowired
    ProductService productService;


    @GetMapping(value = "/createPdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createPdf(Principal principal) {
        List<Product> products = productService.findAll();
        ByteArrayInputStream bis = GeneratePdfReport.productReport(products);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=productreport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
