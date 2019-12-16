package com.project.onlinestore.buyer.domain;

import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.seller.domain.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "SELLER_FOLLOWERS",joinColumns = @JoinColumn(name = "followers_id"),inverseJoinColumns = @JoinColumn(name = "seller_id"))
    private List<Seller> following;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;

    private Long points=0L;
}
