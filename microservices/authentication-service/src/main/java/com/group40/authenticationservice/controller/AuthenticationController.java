package com.group40.authenticationservice.controller;

import com.group40.authenticationservice.dto.PersonRequest;
import com.group40.authenticationservice.dto.PersonResponse;
import com.group40.authenticationservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(@RequestBody PersonRequest personRequest) {
        authenticationService.createPerson(personRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllProducts() {
        return authenticationService.getAllPersons();
    }

}