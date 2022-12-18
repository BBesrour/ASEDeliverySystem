package com.group40.deliveryservice.dto;

import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.ERole;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NonNull
    private String email;

    @NonNull
    private ERole role;

    private List<Box> boxes;

    private List<Delivery> deliveries;

    private String password;
}
