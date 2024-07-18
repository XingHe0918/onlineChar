package com.example.loginservice.model.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private Long id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private String privilege;

    private String isDelete;

    public User(){};

    public User(Long id,String username, String password, String privilege, String isDelete) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.privilege = privilege;
        this.isDelete = isDelete;
    }

}
