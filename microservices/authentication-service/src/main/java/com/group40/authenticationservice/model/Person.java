package com.group40.authenticationservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(value = "person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person {
    @Id
    private String id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();

}
