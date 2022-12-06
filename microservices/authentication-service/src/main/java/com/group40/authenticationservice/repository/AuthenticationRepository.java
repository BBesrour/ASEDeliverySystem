package com.group40.authenticationservice.repository;

import com.group40.authenticationservice.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticationRepository extends MongoRepository<Person, String> {
}
