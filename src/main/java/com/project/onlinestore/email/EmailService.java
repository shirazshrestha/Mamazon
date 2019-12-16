package com.project.onlinestore.email;

import com.project.onlinestore.review.domain.Review;
import com.project.onlinestore.security.domain.User;

public interface EmailService {
    void sendPendingAcceptanceEmail(User seller);
    void sendPendingRejectEmail(User seller);
    void sendPendingAcceptanceReview(User buyer, Review review);
    void sendPendingRejectReview(User buyer, Review review);
}
