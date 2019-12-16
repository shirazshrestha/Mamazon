package com.project.onlinestore.admin.service;

import com.project.onlinestore.admin.domain.Ad;
import com.project.onlinestore.security.domain.User;

import java.util.List;

public interface AdminService {
    List<User> getPendingSellers();
    void acceptSeller(Long sellerId);
    void rejectSeller(Long sellerId);
    void changeAd(Ad ad, String rootDirectory);
}
