package com.group40.deliveryservice.dto;

import com.group40.deliveryservice.model.DeliveryStatus;
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

    private boolean active;
    private String targetCustomerID;
    private String targetBoxID;
    private String delivererID;
    private DeliveryStatus status;
}
