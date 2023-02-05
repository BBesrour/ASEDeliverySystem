package com.group40.authenticationservice.controller;

import com.group40.authenticationservice.dto.request.PersonRequest;
import com.group40.authenticationservice.dto.response.PersonResponse;
import com.group40.authenticationservice.model.ERole;
import com.group40.authenticationservice.model.Role;
import com.group40.authenticationservice.model.User;
import com.group40.authenticationservice.repository.RoleRepository;
import com.group40.authenticationservice.repository.UserRepository;
import com.group40.authenticationservice.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<?> updateUser(@RequestBody PersonRequest newUser, @PathVariable String id){
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            return ResponseEntity.notFound().build();
        User user = userOpt.get();
        user.setEmail(newUser.getEmail());
        if (!newUser.getPassword().isBlank())
            user.setPassword(encoder.encode(newUser.getPassword()));
        Role role = roleRepository.findByName(ERole.valueOf(newUser.getRole().toUpperCase()))
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRole(role);
        user = userRepository.save(user);

        PersonResponse response = PersonResponse.builder()
                .id(user.getId())
                .token(user.getToken())
                .role(user.getRole().getName().name())
                .email(user.getEmail()).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            return ResponseEntity.notFound().build();
        User user = userOpt.get();

        userRepository.deleteById(id);

        PersonResponse response = PersonResponse.builder()
                .id(user.getId()).
                role(user.getRole().getName().name())
                .email(user.getEmail()).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISPATCHER')")
    ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<PersonResponse> responses = users.stream()
                .map(u -> new PersonResponse(u.getId(), u.getEmail(), u.getRole().getName().name(), u.getToken()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user-to-token")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('DISPATCHER') or hasRole('DELIVERER')")
    public ResponseEntity<?> getTokenFromUser() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String token = userDetails.getToken();
        return ResponseEntity.ok(token);
    }
    @GetMapping("/token-to-user")
    public ResponseEntity<?> getUserFromToken(@RequestParam String token){
        User user = userRepository.findByToken(token).orElseThrow();
        PersonResponse response = PersonResponse.builder()
                .id(user.getId()).
                role(user.getRole().getName().name())
                .email(user.getEmail()).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<?> getUser(@PathVariable String id){
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            return ResponseEntity.notFound().build();
        User user = userOpt.get();
        PersonResponse response = PersonResponse.builder()
                .id(user.getId()).
                role(user.getRole().getName().name())
                .email(user.getEmail())
                .token(user.getToken())
                .build();
        return ResponseEntity.ok(response);
    }
}
