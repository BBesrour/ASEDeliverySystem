package com.group40.authenticationservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.group40.authenticationservice.model.User;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  Optional<User> findByToken(String token);
}
