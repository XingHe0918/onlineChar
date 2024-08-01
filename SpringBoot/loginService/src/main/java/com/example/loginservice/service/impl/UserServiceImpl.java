package com.example.loginservice.service.impl;



import com.example.loginservice.config.UidGeneratorConfig;
import com.example.loginservice.jwt.JwtTokenUtil;
import com.example.loginservice.model.dao.User;
import com.example.loginservice.model.dao.UserDetail;
import com.example.loginservice.model.vo.UserVO;
import com.example.loginservice.repository.UserDetailRepository;
import com.example.loginservice.repository.UserRepository;
import com.example.loginservice.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
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
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private QQEmailServiceImpl qqEmailService;
    @Autowired
    private RedisServiceImpl redisService;

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
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

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
    public ResponseEntity<?> addUser(UserVO userVO) {
        System.out.println(userVO.getHeadImage());
        //验证码校验
//        if(!redisService.getValue(userVO.getEmail()).equals(userVO.getVerificationCode())){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code is incorrect");
//        }
        //用户名密码校验
        if(userVO.getUsername() == null || userVO.getUsername().isEmpty() || userVO.getPassword() == null || userVO.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password cannot be empty");
        }

        //用户是否存在校验
        if(
                userRepository.existsByUsername(userVO.getUsername())
//                || userDetailRepository.existsByUsername(user.getUsername())
        ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(userVO.getPassword());
        //id生成
        Long id = uidGeneratorConfig.nextId();

        //sql插入
        userRepository.save(
                new User(
                        id,
                        userVO.getUsername(),
                        encryptPassword,
                        "ROLE_USER",
                        "0"));
        userDetailRepository.save(
                new UserDetail(userVO, id)
        );
        return ResponseEntity.ok("User registered successfully");
    }

    @Override
    public ResponseEntity<?> getVerificationCode(String email) {
        //校验邮箱格式
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if(!email.matches(regex)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }
        if(email != null && !email.isEmpty()){
            if(redisService.hasKey(email)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Verification code already sent, please check your email");
            }
            String code = RandomStringUtils.randomNumeric(6);
            redisService.setValueAndExpire(email, code, 5 * 60, TimeUnit.SECONDS);
            qqEmailService.sendSimpleEmail(email, "Verification Code", "Your verification code is: " + code);
            // TODO: Send verification code to email
            return ResponseEntity.ok("Verification code sent to email");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }
    }


//    @Override
//    public User getUser(int id){
//        return userMapper.selectById(id);
//    }
}
