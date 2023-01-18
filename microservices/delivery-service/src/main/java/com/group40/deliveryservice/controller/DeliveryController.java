package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.EmailDetails;
import com.group40.deliveryservice.service.DeliveryService;
import com.group40.deliveryservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Delivery newDelivery(@RequestBody Delivery newDelivery) {
        return deliveryService.saveDelivery(newDelivery);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Delivery getSingleDelivery(@PathVariable(value = "id") String id) {
        return deliveryService.getSingleDelivery(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Delivery replaceDelivery(@RequestBody Delivery newDelivery, @PathVariable String id) {
        return deliveryService.replaceDelivery(newDelivery, id);
    }

    @DeleteMapping("/{id}")
    void deleteDelivery(@PathVariable String id) {
        deliveryService.deleteDelivery(id);
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    List<Delivery> active(
            @RequestParam(value = "customer") String customer
    ) {
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
