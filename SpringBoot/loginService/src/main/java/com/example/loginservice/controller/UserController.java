package com.example.loginservice.controller;

import com.example.loginservice.model.dao.User;
import com.example.loginservice.model.vo.UserVO;
import com.example.loginservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.sax.SAXResult;
import java.util.Map;

@RestController
@RequestMapping("/login-service")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/demo")
    public String demo() {
        return "Hello World";
    }


    //用户注册
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserVO user) {
        return userService.addUser(user);
    }

    //用户登录
    @PostMapping("/userRegister")
    public ResponseEntity<?> userLogin(@RequestBody User loginUser)  {
        return userService.userLogin(loginUser.getUsername(), loginUser.getPassword());
    }


    //使用redis缓存验证码，时效5min，以邮箱号码为凭证 结构(邮箱号码, 验证码)
    @PostMapping("/getVerificationCode")
    public ResponseEntity<?> getVerificationCode(@RequestBody Map<String, String> map) {
        return userService.getVerificationCode(map.get("email"));
    }



}
