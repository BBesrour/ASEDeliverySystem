package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createBox(@RequestBody BoxRequest boxRequest) {
        deliveryService.createBox(boxRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BoxResponse> getAllBoxes() {
        return deliveryService.getAllBoxes();
    }


    @GetMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public BoxResponse getBox(@RequestBody String id) throws Exception  { return deliveryService.getBox(id);}

    @PutMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public void updateBox(@RequestBody BoxRequest boxRequest) throws Exception {
        deliveryService.updateBox(boxRequest);
    }

}
