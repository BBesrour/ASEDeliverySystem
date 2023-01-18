package com.group40.deliveryservice.repository;

import com.group40.deliveryservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'role' : ?0 }")
    List<User> findByRole(String role);

    Boolean existsByEmail(String email);

    User findByEmail(String email);
}
