package com.project.onlinestore.buyer.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private Address shippingAddress;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Valid
    private Address billingAddress;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Valid
    private Payment payment;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Buyer buyer;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Line> lines;

    private int status=1;

    private LocalDateTime localDateTime;

    private Long pointUsed=0L;

    public Double getTotal(){
        Double total = 0.0;
        for (Line line:lines){
            total += line.getPrice();
        }
        return total-(double) pointUsed/100;
    }

    public String getStatusText() {
        switch (this.status) {
            case 1:
                return "pending";
            case 2:
                return "on way";
            case 3:
                return "delivred";
            case 4:
                return "canceled";
            default:
                return "undefined";
        }
    }

}
