package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.service.BoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/delivery/box")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createBox(@RequestBody BoxRequest boxRequest) {
        boxService.createBox(boxRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BoxResponse> getAllBoxes() {
        return boxService.getAllBoxes();
    }


    @GetMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public BoxResponse getBox(@RequestParam String id) throws Exception  { return boxService.getBox(id);}

    @PostMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public BoxResponse updateBox(@RequestParam String id , @RequestBody Map<String, String> obj) throws Exception {
        return boxService.updateBox(id, obj);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Box replaceBox(@RequestBody Box newBox, @PathVariable String id) {
        return boxService.replaceBox(newBox, id);
    }

}
