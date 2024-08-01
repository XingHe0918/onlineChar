package com.example.loginservice.repository;

import com.example.loginservice.model.dao.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

}
