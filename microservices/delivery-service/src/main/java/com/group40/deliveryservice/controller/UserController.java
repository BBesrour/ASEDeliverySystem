package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.UserRequest;
import com.group40.deliveryservice.model.Customer;
import com.group40.deliveryservice.model.Deliverer;
import com.group40.deliveryservice.model.Dispatcher;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.repository.UserRepository;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: Change to ResponseEntity

@RestController
@RequestMapping("/api/delivery/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Customer> getAllCustomers() {
        return userService.getAllCustomers();
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    User updateUser(@RequestBody UserRequest newUser, @PathVariable String id) {
        switch (newUser.getRole()) {
            case ROLE_DELIVERER -> {
                Deliverer deliverer = Deliverer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return userService.updateUser(deliverer, id);
            }
            case ROLE_DISPATCHER -> {
                Dispatcher dispatcher = Dispatcher.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return userService.updateUser(dispatcher, id);
            }
            default -> {
                Customer customer = Customer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return userService.updateUser(customer, id);
            }
        }
    }

    @PostMapping
    User createUser(@RequestBody UserRequest newUser) {
        //TODO: Better handling
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }
        if(newUser.getPassword() == null || newUser.getPassword().isBlank() || newUser.getPassword().trim().isBlank()){
            throw new RuntimeException("Creating a new user requires a password.");
        }
        switch (newUser.getRole()) {
            case ROLE_DELIVERER -> {
                Deliverer deliverer = Deliverer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return userService.createUser(deliverer, newUser.getPassword());
            }
            case ROLE_DISPATCHER -> {
                Dispatcher dispatcher = Dispatcher.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return userService.createUser(dispatcher, newUser.getPassword());
            }
            default -> {
                Customer customer = Customer.builder()
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build();
                return userService.createUser(customer, newUser.getPassword());
            }
        }
    }

}
