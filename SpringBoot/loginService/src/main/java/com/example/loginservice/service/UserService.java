package com.example.loginservice.service;

import com.example.loginservice.model.bean.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails loadUserByUsername(String username);

    ResponseEntity<?> userLogin(String userName, String password);

    ResponseEntity<?> addUser(User user);

//    User getUser(int id);
}
