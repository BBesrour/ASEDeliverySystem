package com.group40.deliveryservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class User {
    @Id
    private String id;

    @NonNull
    private String email;

    @NonNull
    private ERole role;
}
