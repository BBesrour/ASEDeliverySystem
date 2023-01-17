package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.ERole;
import com.group40.deliveryservice.model.EmailDetails;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.service.DeliveryService;
import com.group40.deliveryservice.service.EmailService;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/delivery/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @Autowired
    private EmailService emailService;

    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Delivery> getAllDeliveries(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)){
            return deliveryService.getAllDeliveries();
        }
        return deliveryService.getDeliveriesForCustomer(user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> newDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Delivery newDelivery) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER) || newDelivery.getTargetCustomerID().equals(user.getId())) {
            return ResponseEntity.ok(deliveryService.saveDelivery(newDelivery));
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> getSingleDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable(value = "id") String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        Delivery delivery = deliveryService.getSingleDelivery(id);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER) || delivery.getTargetCustomerID().equals(user.getId())) {
            return ResponseEntity.ok(deliveryService.getSingleDelivery(id));
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> replaceDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Delivery newDelivery, @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        Delivery delivery = deliveryService.getSingleDelivery(id);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER) || delivery.getTargetCustomerID().equals(user.getId())) {
            return ResponseEntity.ok(deliveryService.replaceDelivery(newDelivery, id));
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        Delivery delivery = deliveryService.getSingleDelivery(id);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER) || delivery.getTargetCustomerID().equals(user.getId())) {
            deliveryService.deleteDelivery(id);
            return ResponseEntity.ok("Delivery deleted");
        }else {
            return ResponseEntity.badRequest().body("Not authorized!");
        }
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    List<Delivery> active(@RequestParam(value = "customer") String customer) {
        return deliveryService.getActiveDeliveries(customer);
    }

    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    List<Delivery> inactive(
            @RequestParam(value = "customer") String customer
    ) {
        return deliveryService.getInactiveDeliveries(customer);
    }

}
