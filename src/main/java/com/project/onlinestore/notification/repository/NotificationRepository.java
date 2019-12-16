package com.project.onlinestore.notification.repository;

import com.project.onlinestore.notification.domain.Notification;
import com.project.onlinestore.security.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification,Long> {
    int countAllByUserAndSeenIsFalse(User user);
    List<Notification> findByUserOrderByLocalDateTimeDesc(User user);
    List<Notification> findByUser(User user);
}
