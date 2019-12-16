package com.project.onlinestore.review.domain;

import com.project.onlinestore.buyer.domain.Buyer;
import com.project.onlinestore.product.domain.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private LocalDateTime localDateTime;

    @NotBlank
    private String title;

    private Integer rate=0;

    @NotBlank
    private String comment;

    private int status=1;
}
