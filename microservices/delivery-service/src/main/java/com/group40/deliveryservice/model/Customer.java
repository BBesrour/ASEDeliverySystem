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
    public Customer(String email, ERole role, List<Delivery> deliveries, List<Box> boxes){
        super(email, role);
        this.boxes = boxes;
        this.deliveries = deliveries;
    }
    @DBRef
    private List<Delivery> deliveries;

    @DBRef
    private List<Box> boxes;

    //TODO: add token attribute


}
