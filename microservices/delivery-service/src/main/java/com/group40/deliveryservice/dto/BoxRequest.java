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
public class BoxRequest {
    private String id;
    private String name;
    private String key;
    private String assigned_to;
    private String assigned_by;
    private String address;
    private ArrayList<String> assigned_customers;
}