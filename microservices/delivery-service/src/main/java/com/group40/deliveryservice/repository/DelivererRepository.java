package com.group40.deliveryservice.repository;


import com.group40.deliveryservice.model.Customer;
import com.group40.deliveryservice.model.Deliverer;
import com.group40.deliveryservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelivererRepository extends MongoRepository<Deliverer, String> {


    Boolean existsByEmail(String email);

    Deliverer findByEmail(String email);

    @Query("{'token':  ?0 }")
    Deliverer findByToken(String token);
}

