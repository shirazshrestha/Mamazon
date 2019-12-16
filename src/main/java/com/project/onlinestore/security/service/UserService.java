package com.project.onlinestore.security.service;


import com.project.onlinestore.security.domain.User;

public interface UserService {

    User saveUser(User user);

    User findUserByUserName(String username);
}
