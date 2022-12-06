package com.group40.authenticationservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person {
    @Id
    private String id;
    private String email;
    private String password;
}
