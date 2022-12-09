package com.group40.deliveryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String assigned_to;
    private String assigned_by;
    private String[] assigned_customers;
    private String key;
}

