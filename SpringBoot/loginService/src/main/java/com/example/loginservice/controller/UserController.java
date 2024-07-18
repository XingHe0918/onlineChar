package com.example.loginservice.controller;

import com.example.loginservice.model.bean.User;
import com.example.loginservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/demo")
    public String demo() {
        return "Hello World";
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/userRegister")
    public ResponseEntity<?> userLogin(@RequestBody User loginUser)  {
        System.out.println(loginUser);
        return userService.userLogin(loginUser.getUsername(), loginUser.getPassword());
    }


}
