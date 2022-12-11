package com.group40.authenticationservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.group40.authenticationservice.model.Person;

public interface UserRepository extends MongoRepository<Person, String> {

  Optional<Person> findByEmail(String email);

  Boolean existsByEmail(String email);
}
