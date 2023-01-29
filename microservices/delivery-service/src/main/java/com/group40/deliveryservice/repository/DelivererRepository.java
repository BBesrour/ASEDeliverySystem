package com.group40.deliveryservice.repository;

import com.group40.deliveryservice.model.Deliverer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DelivererRepository extends MongoRepository<Deliverer, String> {


    Boolean existsByEmail(String email);

    Deliverer findByEmail(String email);

    @Query("{'token':  ?0 }")
    Deliverer findByToken(String token);
}

