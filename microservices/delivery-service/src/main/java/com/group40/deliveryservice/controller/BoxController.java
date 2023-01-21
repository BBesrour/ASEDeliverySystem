package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.dto.PersonResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.ERole;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.service.BoxService;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/delivery/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;
    @Autowired
    private final UserService userService;

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

    @GetMapping("/deliverer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<BoxResponse> getBoxesByDeliverer(@PathVariable String id) {
        return boxService.getBoxesByDeliverer(id);
    }

    @DeleteMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBox(@RequestParam String id) throws Exception {
        boxService.deleteBox(id);
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
