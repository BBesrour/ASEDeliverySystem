package com.group40.deliveryservice.dto;
import com.group40.deliveryservice.model.ERole;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NonNull
    private String email;

    @NonNull
    private ERole role;

    private String password;
}
