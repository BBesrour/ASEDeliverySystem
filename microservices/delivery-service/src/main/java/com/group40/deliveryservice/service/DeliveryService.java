package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private List<Box> getBoxes(){
        return deliveryRepository.findAll();
    }

    private BoxResponse mapToBoxResponse(Box box) {
        return BoxResponse.builder()
                .address(box.getAddress())
                .name(box.getName())
                .key(box.getKey())
                .assigned_by(box.getAssigned_by())
                .assigned_to(box.getAssigned_to())
                .assigned_customers(box.getAssigned_customers())
                .build();
    }

    private Box mapFromBoxRequest(BoxRequest boxRequest) {
        return Box.builder()
                .address(boxRequest.getAddress())
                .name(boxRequest.getName())
                .key(boxRequest.getKey())
                .assigned_by(boxRequest.getAssigned_by())
                .assigned_to(boxRequest.getAssigned_to())
                .assigned_customers(boxRequest.getAssigned_customers())
                .build();
    }

    public void createBox(BoxRequest boxRequest) {
        Box box = Box.builder()
                .id(UUID.randomUUID().toString())
                .address(boxRequest.getAddress())
                .name(boxRequest.getName())
                .key(boxRequest.getKey())
                .assigned_by(boxRequest.getAssigned_by())
                .assigned_to(boxRequest.getAssigned_to())
                .assigned_customers(boxRequest.getAssigned_customers())
                .build();

        deliveryRepository.insert(box);
    }

    public List<BoxResponse> getAllBoxes() {
//        List<Box> boxes = deliveryRepository.findAll();

        return getBoxes().stream().map(this::mapToBoxResponse).toList();
    }



    public BoxResponse getBox(String id) throws Exception {
//        List<Box> boxes = deliveryRepository.findAll();

        Box box = getBoxes().stream()
                .filter(box1 -> Objects.equals(id, box1.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("Box cannot found"));
        return mapToBoxResponse(box);
    }

    private Box updateField(String key, String value, Box box) {
        switch(key) {
            case "name":
                box.setName(value);
                break;
            case "key":
                box.setKey(value);
                break;
            case "assigned_to":
                box.setAssigned_to(value);
                break;
            case "assigned_by":
                box.setAssigned_by(value);
                break;
            case "address":
                box.setAddress(value);
                break;
            case "assigned_customers":
                box.getAssigned_customers().add(value);
                break;
        }
        return box;
    }

    public BoxResponse updateBox(String id, Map<String, String> obj) throws Exception {
        for (int i=0; i<getBoxes().size(); i++) {
            Box b = getBoxes().get(i);
            if (Objects.equals(b.getId(), id)) {
                for(String key: obj.keySet()) {
                    b = updateField(key, obj.get(key), b);
                }
                deliveryRepository.save(b);
                return mapToBoxResponse(b);
            }
        }
        throw new Exception("Box ID does not exist!!!");
    }
}