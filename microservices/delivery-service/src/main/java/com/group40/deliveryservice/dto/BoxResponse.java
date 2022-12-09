package com.group40.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxResponse {
    private String id;
    private String name;
    private String address;
    private String assigned_to;
    private String assigned_by;
    private String[] assigned_customers;
    private String key;
}
