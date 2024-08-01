package com.example.loginservice.model.vo;


// TODO: 2022/11/22  UserVO
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String gender;

    private String phoneNumber;

    private String email;

    private String address;

    private String password;

    private String verificationCode;

    private String headImage;

    private String role;


}
