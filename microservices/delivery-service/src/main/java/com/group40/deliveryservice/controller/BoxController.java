package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.dto.PersonResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.model.ERole;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.service.BoxService;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/delivery/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    private final UserService userService;
    @Value("${adminToken}")
    private String adminToken;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody BoxRequest boxRequest) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.createBox(boxRequest));
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllBoxes(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.getAllBoxes());
        }else if (user.getRole().equals(ERole.ROLE_DELIVERER)){
            return ResponseEntity.ok(boxService.getBoxesByDeliverer(user.getId()));
        }else if (user.getRole().equals(ERole.ROLE_CUSTOMER)){
            return ResponseEntity.ok(boxService.getBoxesByCustomer(user.getId()));
        } else{
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }


    @GetMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String id) throws Exception {
        User user = userService.getUser(token);
        BoxResponse box = boxService.getBox(id);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER) ||
                (box.getAssignedTo().equals(user.getId()) && user.getRole().equals(ERole.ROLE_DELIVERER)) ||
                (box.getAssignedCustomers().contains(user.getId()) && user.getRole().equals(ERole.ROLE_CUSTOMER))) {
            return ResponseEntity.ok(box);
        } else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }

    }

    @PostMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @RequestParam String id, @RequestBody Map<String, String> obj) throws Exception {
        String adminTokenCheck = "Bearer " + adminToken;
        if (adminTokenCheck.equals(token)) {
            return ResponseEntity.ok(boxService.updateBox(id, obj));
        }

        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.updateBox(id, obj));
        } else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> replaceBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                   @RequestBody Box newBox, @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.replaceBox(newBox, id));
        } else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @GetMapping("/deliverer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBoxesByDeliverer(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DELIVERER)) {
            return ResponseEntity.ok(boxService.getBoxesByDeliverer(id));
        } else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @PathVariable String id) throws Exception {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            boxService.deleteBox(id);
            return ResponseEntity.ok("Box deleted!");
        } else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }

    }

    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse getUserDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);

        return PersonResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .id(user.getId())
                .build();
    }

}
