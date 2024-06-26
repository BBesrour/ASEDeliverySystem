package com.group40.deliveryservice.repository;


import com.group40.deliveryservice.model.Customer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Boolean existsByEmail(String email);

    Customer findByEmail(String email);

    @Query("{'token':  ?0 }")
    Customer findByToken(String token);
}

