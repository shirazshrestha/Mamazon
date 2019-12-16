package com.project.onlinestore.notification.domain;

import com.project.onlinestore.security.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private LocalDateTime localDateTime;

    private String link;

    private boolean seen;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
