package com.project.onlinestore.buyer.domain;

import com.project.onlinestore.product.domain.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int qty;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    public Double getPrice(){
        return qty*product.getPrice();
    }
}
