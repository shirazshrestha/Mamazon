package com.project.onlinestore.email;

import com.project.onlinestore.review.domain.Review;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.utils.EmailConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender emailSender;

    @Override
    public void sendPendingAcceptanceEmail(User seller) {
        String to = seller.getEmail();
        String subject = EmailConstants.EMAIL_ACCEPT_SUBJECT;
        String text = EmailConstants.EMAIL_ACCEPT_TEXT_START+seller.getUsername()+EmailConstants.EMAIL_ACCEPT_TEXT_END;

        sendSimpleMessage(to,subject,text);
    }

    @Override
    public void sendPendingRejectEmail(User seller) {
        String to = seller.getEmail();
        String subject = EmailConstants.EMAIL_REJECT_SUBJECT;
        String text = EmailConstants.EMAIL_REJECT_TEXT_START+seller.getUsername()+EmailConstants.EMAIL_REJECT_TEXT_END;

        sendSimpleMessage(to,subject,text);
    }

    @Override
    public void sendPendingAcceptanceReview(User buyer, Review review) {
        String to = buyer.getEmail();
        String subject = "[Review Accepted]";
        String text = "Dear Mr "+buyer.getUsername()+","+"\nYour review #"+review.getId()+" for product "+review.getProduct().getTitle()+" has been approved!";

        sendSimpleMessage(to,subject,text);
    }

    @Override
    public void sendPendingRejectReview(User buyer,Review review) {
        String to = buyer.getEmail();
        String subject = "[Review Accepted]";
        String text = "Dear Mr "+buyer.getUsername()+","+"\nYour review #"+review.getId()+" for product "+review.getProduct().getTitle()+" has been rejected!";

        sendSimpleMessage(to,subject,text);
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


}
