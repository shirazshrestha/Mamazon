package com.project.onlinestore.product.service.impl;

import com.project.onlinestore.notification.service.NotificationService;
import com.project.onlinestore.product.domain.Product;
import com.project.onlinestore.product.repository.ProductRepository;
import com.project.onlinestore.product.service.ProductService;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.service.UserService;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.seller.repository.SellerRepository;
import com.project.onlinestore.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private NotificationService notificationService;


    @Override
    public Product save(Product product) {
        Product newProduct =productRepository.save(product);
        notificationService.notifyAllFollowers(product.getSeller(),"A new product "+product.getTitle()+" has been added by "+product.getSeller().getUser().getUsername(),
                "http://localhost:8080/products/details/"+product.getId());
        return newProduct;
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> findProductsBySeller(Seller seller) {
        return productRepository.findProductsBySeller(seller);
    }

}
