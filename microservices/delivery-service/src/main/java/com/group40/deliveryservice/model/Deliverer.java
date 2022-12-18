package com.group40.deliveryservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(value = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Deliverer extends User{

    @Builder
    public Deliverer(String email, ERole role, List<Delivery> deliveries){
        super(email, role);
        this.deliveries = deliveries;
    }
    @DBRef
    private List<Delivery> deliveries;

    //TODO: add token attribute
}