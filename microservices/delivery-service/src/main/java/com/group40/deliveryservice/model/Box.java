package com.group40.deliveryservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document(value = "box")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Box {
    @Id
    private String id;
    private String name;
    private String key;
    private String assignedTo;
    private String assignedBy;
    private String address;
    private ArrayList<String> assignedCustomers;
}

