package com.example.loginservice.repository;

import com.example.loginservice.model.dao.UserDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailRepository extends MongoRepository<UserDetail, Long> {

    UserDetail findUserDetailByUsername(String username);

    boolean existsByUsername(String username);
}
