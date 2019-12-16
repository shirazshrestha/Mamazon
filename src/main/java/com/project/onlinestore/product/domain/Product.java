package com.project.onlinestore.product.domain;

import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.seller.domain.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 2, max = 50)
    private String title;
    @Size(min=4, max=200)
    private String description;
    @NotNull
    @Min(1)
    private Double price;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    @Transient
    private MultipartFile image;
}
