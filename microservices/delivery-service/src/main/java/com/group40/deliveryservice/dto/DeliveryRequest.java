package com.group40.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {
    private String id;

    private boolean isActive;
    private String targetCustomerID;
    private String targetBoxID;
    private String delivererID;
}
