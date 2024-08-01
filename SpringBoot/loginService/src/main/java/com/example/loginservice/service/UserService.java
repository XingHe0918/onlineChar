package com.example.loginservice.service;

import com.example.loginservice.model.dao.User;
import com.example.loginservice.model.dao.UserDetail;
import com.example.loginservice.model.vo.UserVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails loadUserByUsername(String username);

    ResponseEntity<?> userLogin(String userName, String password);

    ResponseEntity<?> addUser(UserVO userVO);

    ResponseEntity<?> getVerificationCode(String email);

//    User getUser(int id);
}
