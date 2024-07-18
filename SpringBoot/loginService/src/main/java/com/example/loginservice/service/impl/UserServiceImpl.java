package com.example.loginservice.service.impl;



import com.example.loginservice.config.UidGeneratorConfig;
import com.example.loginservice.jwt.JwtTokenUtil;
import com.example.loginservice.model.bean.User;
import com.example.loginservice.repository.UserRepository;
import com.example.loginservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UidGeneratorConfig uidGeneratorConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        // 获取用户的角色，并转换为GrantedAuthority对象
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getPrivilege()));

        // 创建UserDetails对象并设置用户名、密码和权限
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
        return userDetails;
    }

    @Override
    public ResponseEntity<?> userLogin(String userName, String password) {
        if (userName != null && !userName.isEmpty() && password != null && !password.isEmpty()){
            UserDetails userDetails = loadUserByUsername(userName);
            if(userDetails == null)return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户不存在");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password,userDetails.getPassword())) {
                // 返回令牌给客户端
                Map<String, String> response = new HashMap<>();
                response.put("AccessToken", jwtTokenUtil.generateAccessToken(userName));
                response.put("RefreshToken", jwtTokenUtil.generateRefreshToken(userName));
                return ResponseEntity.ok(response);
                // 密码匹配，返回用户信息
            } else {
                // 密码不匹配，返回错误信息
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("密码错误");
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户和密码不能为空");
        }
        // 检查密码是否匹配

    }


    @Override
    public ResponseEntity<?> addUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(user.getPassword());
        userRepository.save(
                new User(
                        uidGeneratorConfig.nextId(),
                        user.getUsername(),
                        encryptPassword,
                        "ROLE_USER",
                        "0"));
        return ResponseEntity.ok("User registered successfully");
    }


//    @Override
//    public User getUser(int id){
//        return userMapper.selectById(id);
//    }
}
