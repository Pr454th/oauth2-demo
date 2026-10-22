package com.example.oauth2_demo.repository;

import com.example.oauth2_demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query method to find all users
    List<User> findAll();
}
