package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.PersonResponse;
import com.group40.deliveryservice.dto.UserRequest;
import com.group40.deliveryservice.model.*;
import com.group40.deliveryservice.repository.UserRepository;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/api/delivery/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final SecureRandom secureRandom;

    private final Base64.Encoder base64Encoder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> getAllCustomers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(userService.getAllCustomers());
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserRequest newUser, @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (!user.getRole().equals(ERole.ROLE_DISPATCHER) && !user.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
        switch (newUser.getRole()) {
            case ROLE_DELIVERER -> {
                Deliverer deliverer = Deliverer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return ResponseEntity.ok(userService.updateUser(deliverer, id));
            }
            case ROLE_DISPATCHER -> {
                Dispatcher dispatcher = Dispatcher.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return ResponseEntity.ok(userService.updateUser(dispatcher, id));
            }
            default -> {
                Customer customer = Customer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return ResponseEntity.ok(userService.updateUser(customer, id));
            }
        }
    }

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody UserRequest newUser) {
        //TODO: Better handling
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }
        if(newUser.getPassword() == null || newUser.getPassword().isBlank() || newUser.getPassword().trim().isBlank()){
            throw new RuntimeException("Creating a new user requires a password.");
        }
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        switch (newUser.getRole()) {
            case ROLE_DELIVERER -> {
                Deliverer deliverer = Deliverer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .token(base64Encoder.encodeToString(randomBytes))
                        .build();
                return ResponseEntity.ok(userService.createUser(deliverer, newUser.getPassword()));
            }
            case ROLE_DISPATCHER -> {
                Dispatcher dispatcher = Dispatcher.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return ResponseEntity.ok(userService.createUser(dispatcher, newUser.getPassword()));
            }
            default -> {
                Customer customer = Customer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .token(base64Encoder.encodeToString(randomBytes))
                        .build();
                return ResponseEntity.ok(userService.createUser(customer, newUser.getPassword()));
            }
        }
    }

    @GetMapping("/token")
    public ResponseEntity<?> getUserToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);
        switch (user.getRole()){
            case ROLE_DELIVERER -> {
                return ResponseEntity.ok(((Deliverer) user).getToken());
            }
            case ROLE_CUSTOMER -> {
                return ResponseEntity.ok(((Customer) user).getToken());
            }
            default -> {
                return ResponseEntity.badRequest()
                        .body("Incorrect role");
            }
        }
    }

}
