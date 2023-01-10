package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.dto.PersonResponse;
import com.group40.deliveryservice.service.BoxService;
import lombok.RequiredArgsConstructor;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/delivery/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BoxResponse createBox(@RequestBody BoxRequest boxRequest) {
        return boxService.createBox(boxRequest);
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

    @DeleteMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBox(@RequestParam String id) throws Exception {
        boxService.deleteBox(id);
    }

    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse getUserDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        return boxService.getUser(token);
//        return null;
    }

}
