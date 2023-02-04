package com.group40.deliveryservice.repository;

import com.group40.deliveryservice.model.Box;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BoxRepository extends MongoRepository<Box, String> {
    @Query("{ 'name' : ?0 }")
    Box findByName(String name);
}
