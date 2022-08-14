package com.prashant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prashant.entity.User;

public interface UserRepository  extends JpaRepository<User, Long>{
Optional<User> findByEmail(String email);
Optional<User> findByUsernameOrEmail(String name,String email);
Optional<User> findByUsername(String name);
Boolean existsByusername(String name);
Boolean existsByEmail(String email);
}
