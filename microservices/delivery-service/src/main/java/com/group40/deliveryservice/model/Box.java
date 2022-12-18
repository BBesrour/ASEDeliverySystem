package com.group40.deliveryservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "box")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Box {
    @Id
    private String id;
    private String name;
    private String address;
    private int number_of_items;
    private String status;
}