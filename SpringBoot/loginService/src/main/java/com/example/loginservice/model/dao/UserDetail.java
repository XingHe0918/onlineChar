package com.example.loginservice.model.dao;


import com.example.loginservice.model.vo.UserVO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Document(collection = "userDetail")
public class UserDetail {

    @Id
    private Long id;

    @Indexed(unique = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 20, message = "用户名长度必须在5到20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @Size(max = 30, message = "昵称长度不能超过30个字符")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]*$", message = "昵称只能包含汉字、字母、数字和下划线")
    private String nickname;

    @Pattern(regexp = "男|女|其他|不愿透露", message = "性别必须是 '男', '女', '其他' 或 '不愿透露'")
    private String gender;

    @Pattern(regexp = "^\\+?[0-9]*$", message = "电话号码格式不正确")
    private String phoneNumber;

    @Email(message = "无效的电子邮件地址")
    private String email;

    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String address;

    private String headImage; // 头像图片路径

    public UserDetail(UserVO userVO, Long id){
        this.id = id;
        this.username = userVO.getUsername();
        this.nickname = userVO.getNickname();
        this.gender = userVO.getGender();
        this.phoneNumber = userVO.getPhoneNumber();
        this.email = userVO.getEmail();
        this.address = userVO.getAddress();
        this.headImage = userVO.getHeadImage();
    }

}
