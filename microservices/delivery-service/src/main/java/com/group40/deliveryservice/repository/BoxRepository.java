package com.group40.deliveryservice.repository;

import com.group40.deliveryservice.model.Box;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoxRepository extends MongoRepository<Box, String> {
}
