package com.group40.deliveryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "user")
@Data

public class Dispatcher extends User {
    @Builder
    public Dispatcher(String email, ERole role){
        super(email, role);
    }
}
