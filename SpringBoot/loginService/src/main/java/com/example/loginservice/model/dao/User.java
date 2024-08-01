package com.example.loginservice.model.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Document(collection = "users")
public class User {

    @Id
    private Long id;

    @Indexed(unique = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 20, message = "用户名长度必须在5到20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码长度必须至少为8个字符")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).*", message = "密码必须包含一个大写字母、一个小写字母、一个数字和一个特殊字符")
    private String password;

    private String role;

    private String isDelete;

    public User(){};

    public User(Long id,String username, String password, String role, String isDelete) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isDelete = isDelete;
    }

}
