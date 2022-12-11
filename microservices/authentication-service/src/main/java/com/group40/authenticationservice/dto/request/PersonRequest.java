package com.group40.authenticationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
