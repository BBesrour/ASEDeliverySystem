package com.group40.deliveryservice.controller;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.*;
import com.group40.deliveryservice.service.BoxNameDuplicateException;
import com.group40.deliveryservice.service.BoxService;
import com.group40.deliveryservice.service.DeliveryService;
import com.group40.deliveryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
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

    private final DeliveryService deliveryService;

    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody BoxRequest boxRequest) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            try {
                return ResponseEntity.ok(boxService.createBox(boxRequest));
            } catch (BoxNameDuplicateException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"" + e.getMessage() + "\"}");
            }
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllBoxes(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JSONException, IOException {
        User user = userService.getUser(token);
        if (userService.adminTokenIsValid(token)) {
            return ResponseEntity.ok(boxService.getAllBoxes());
        } else if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.getAllBoxes());
        } else if (user.getRole().equals(ERole.ROLE_DELIVERER)) {
            return ResponseEntity.ok(boxService.getBoxesByDeliverer(user.getId()));
        } else if (user.getRole().equals(ERole.ROLE_CUSTOMER)) {
            return ResponseEntity.ok(boxService.getAllBoxes());
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }


    @GetMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String id) throws Exception {
        User user = userService.getUser(token);
        BoxResponse box = boxService.getBox(id);
        List<Delivery> deliveries = deliveryService.getAllDeliveries().stream().filter(delivery -> delivery.getTargetBoxID().equals(id)).toList();

        if (userService.adminTokenIsValid(token) ||
                user.getRole().equals(ERole.ROLE_DISPATCHER) ||
                (!deliveries.stream().filter(del -> del.getDelivererID().equals(user.getId())).toList().isEmpty() && user.getRole().equals(ERole.ROLE_DELIVERER)) ||
                (box.getAssignedCustomer().contains(user.getId()) && user.getRole().equals(ERole.ROLE_CUSTOMER))) {
            return ResponseEntity.ok(box);
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }

    }

    @PostMapping("/box")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @RequestParam String id, @RequestBody Map<String, String> obj) throws Exception {
        if (userService.adminTokenIsValid(token)) {
            return ResponseEntity.ok(boxService.updateBox(id, obj));
        }

        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.updateBox(id, obj));
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> replaceBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                 @RequestBody Box newBox, @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);
        System.out.println(user.getRole() == ERole.ROLE_DISPATCHER);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            try {
                return ResponseEntity.ok(boxService.replaceBox(newBox, id));
            } catch (BoxNameDuplicateException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"Box name already exists!\"}");
            }
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }

    @GetMapping("/deliverer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBoxesByDeliverer(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable String id) throws JSONException, IOException {
        User user = userService.getUser(token);

        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            return ResponseEntity.ok(boxService.getBoxesByDeliverer(id));
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @PathVariable String id) throws Exception {
        User user = userService.getUser(token);
        if (user.getRole().equals(ERole.ROLE_DISPATCHER)) {
            boxService.deleteBox(id);
            return ResponseEntity.ok("{}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
    }

    @PostMapping("/{boxID}/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authenticateBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @PathVariable String boxID, @RequestBody Map<String, String> obj) throws JSONException, IOException {
        if (!userService.adminTokenIsValid(token)) {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
        String userToken = obj.get("userToken");
        User user = userService.getUserFromToken(userToken);
        if (user == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Wrong RFID token!\"}");
        }
        String userId = user.getId();
        List<Delivery> deliveries;
        if (user.getRole().equals(ERole.ROLE_CUSTOMER)) {
            deliveries = deliveryService.getDeliveriesForCustomer(userId);
        } else if (user.getRole().equals(ERole.ROLE_DELIVERER)) {
            deliveries = deliveryService.getDeliveriesForDeliverer(userId);
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized (wrong role)!\"}");
        }
        if (deliveries.stream().filter(del -> del.getTargetBoxID().equals(boxID)).toList().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized (no matching deliveries)!\"}");
        }

        return ResponseEntity.ok("{\"msg\": \"Authenticated!\"}");
    }

    @PostMapping("/{id}/close")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> closeBox(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                      @PathVariable String id,
                                      @RequestBody Map<String, String> obj) throws Exception {
        if (!userService.adminTokenIsValid(token)) {
            return ResponseEntity.badRequest().body("{\"error\": \"Not authorized!\"}");
        }
        String userToken = obj.get("userToken");
        User user = userService.getUserFromToken(userToken);
        if (user == null) {
            return ResponseEntity.badRequest().body("Wrong userToken!");
        }
        DeliveryStatus wantedStatus = null; // no change
        Boolean wantedActive = null; // no change
        BoxResponse box = boxService.getBox(id);
        if (user.getRole().equals(ERole.ROLE_CUSTOMER)) {
            if (box.getAssignedCustomer().equals(user.getId())) {
                boxService.updateTargetCustomer(box.getId(), box.getAssignedCustomer());
                wantedActive = false;
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Not authorized (customer cannot close boxes that are not assigned to them)!\"}");
            }
        } else if (user.getRole().equals(ERole.ROLE_DELIVERER)) {
            wantedStatus = DeliveryStatus.DELIVERED;
        } else {
            return ResponseEntity.badRequest().body("Not authorized (dispatchers cannot close boxes)!");
        }
        return ResponseEntity.ok(deliveryService.changeDeliveriesInBoxStatus(id, wantedStatus, wantedActive, token));
    }
}
