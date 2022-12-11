package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.service.BoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/delivery/box")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBox(@RequestBody BoxRequest boxRequest) {
        boxService.createBox(boxRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BoxResponse> getAllBoxes() {
        return boxService.getAllBoxes();
    }

}
