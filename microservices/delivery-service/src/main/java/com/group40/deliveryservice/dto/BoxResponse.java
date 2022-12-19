package com.group40.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxResponse {
    private String id;
    private String name;
<<<<<<< HEAD
    private String key;
    private String assigned_to;
    private String assigned_by;
    private String address;
    private ArrayList<String> assigned_customers;
=======
    private String address;
    private String assigned_to;
    private String assigned_by;
    private ArrayList<String> assigned_customers;
    private String key;
>>>>>>> 780f05371ab231990e1d178054ff5ddae50e435e
}
