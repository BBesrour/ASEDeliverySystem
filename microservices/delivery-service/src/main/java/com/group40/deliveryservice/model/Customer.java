package com.group40.deliveryservice.model;

import lombok.*;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(value = "user")
@Data
public class Customer extends User {

    @Builder
    public Customer(String email, ERole role){
        super(email, role);
    }
    //TODO: add token attribute


}
