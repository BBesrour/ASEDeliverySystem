package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.exceptions.DeliveryNotFoundException;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.ERole;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.service.DeliveryService;
import com.group40.deliveryservice.service.EmailService;
import com.group40.deliveryservice.service.EmailServiceImpl;
import com.group40.deliveryservice.model.EmailDetails;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/delivery/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final UserService userService;

    private final EmailService emailService;

    @Value("${adminToken}")
    private String adminToken;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Delivery> getAllDeliveries(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)){
            return deliveryService.getAllDeliveries();
        } else if (user.getRole().equals(ERole.ROLE_DELIVERER)){
            return deliveryService.getDeliveriesForDeliverer(user.getId());
        } else if (user.getRole().equals(ERole.ROLE_CUSTOMER)){
            return deliveryService.getDeliveriesForCustomer(user.getId());
        } else {
            return null;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> newDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Delivery newDelivery) throws JSONException, IOException, DeliveryNotFoundException {
        try{
            User user = userService.getUser(token);
            if (user.getRole().equals(ERole.ROLE_DISPATCHER) || newDelivery.getTargetCustomerID().equals(user.getId())) {
                ResponseEntity response_entity = ResponseEntity.ok(deliveryService.saveDelivery(newDelivery));
                EmailDetails email_details = new EmailDetails(user.getEmail(), "New delivery created. Delivery id:"
                        + newDelivery.getId(), "New delivery notification");
                emailService.sendSimpleMail(email_details);
                return response_entity;
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
            }
        } catch( DeliveryNotFoundException e ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"Box does not exist! or assigned to another Customer\"}");
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
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> replaceDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Delivery newDelivery, @PathVariable String id) throws Exception {
        Delivery delivery = deliveryService.getSingleDelivery(id);
        String adminTokenCheck = "Bearer " + adminToken;
        if (adminTokenCheck.equals(token)){
            return ResponseEntity.ok(deliveryService.replaceDelivery(newDelivery, id, token));
        }

        User user = userService.getUser(token);
        try {
            if (user.getRole().equals(ERole.ROLE_DISPATCHER) || delivery.getDelivererID().equals(user.getId()) || delivery.getTargetCustomerID().equals(user.getId())) {
                return ResponseEntity.ok(deliveryService.replaceDelivery(newDelivery, id, token));
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
            } 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Box does not exist! or assigned to another Customer: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteDelivery(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id) throws Exception {
        User user = userService.getUser(token);
        Delivery delivery = deliveryService.getSingleDelivery(id);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER) || delivery.getTargetCustomerID().equals(user.getId())) {
            deliveryService.deleteDelivery(id);
            return ResponseEntity.ok("{}");
        }else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
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
