package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoxService {

    private final BoxRepository boxRepository;

    private List<Box> getBoxes(){
        return boxRepository.findAll();
    }

    private BoxResponse mapToBoxResponse(Box box) {
        return BoxResponse.builder()
                .address(box.getAddress())
                .name(box.getName())
                .key(box.getKey())
                .assignedBy(box.getAssignedBy())
                .assignedTo(box.getAssignedTo())
                .assignedCustomers(box.getAssignedCustomers())
                .build();
    }

    public void createBox(BoxRequest boxRequest) {
        Box box = Box.builder()
                .id(UUID.randomUUID().toString())
                .address(boxRequest.getAddress())
                .name(boxRequest.getName())
                .key(boxRequest.getKey())
                .assignedBy(boxRequest.getAssigned_by())
                .assignedTo(boxRequest.getAssigned_to())
                .assignedCustomers(boxRequest.getAssigned_customers())
                .build();

        boxRepository.insert(box);
    }

    public List<BoxResponse> getAllBoxes() {
        return getBoxes().stream().map(this::mapToBoxResponse).toList();
    }



    public BoxResponse getBox(String id) throws Exception {
        Box box = getBoxes().stream()
                .filter(box1 -> Objects.equals(id, box1.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("Box cannot found"));
        return mapToBoxResponse(box);
    }

    private Box updateField(String key, String value, Box box) {
        switch (key) {
            case "name" -> box.setName(value);
            case "key" -> box.setKey(value);
            case "assigned_to" -> box.setAssignedTo(value);
            case "assigned_by" -> box.setAssignedBy(value);
            case "address" -> box.setAddress(value);
            case "assigned_customers" -> box.getAssignedCustomers().add(value);
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
                boxRepository.save(b);
                return mapToBoxResponse(b);
            }
        }
        throw new Exception("Box ID does not exist!!!");
    }

    public Box replaceBox(Box newBox, String id) {
        return boxRepository.findById(id)
                .map(box -> {
                    box.setName(newBox.getName());
                    box.setKey(newBox.getKey());
                    box.setAssignedBy(newBox.getAssignedBy());
                    box.setAssignedTo(newBox.getAssignedTo());
                    box.setAddress(newBox.getAddress());
                    box.setAssignedCustomers(newBox.getAssignedCustomers());
                    return boxRepository.save(box);
                })
                .orElseGet(() -> {
                    newBox.setId(id);
                    return boxRepository.save(newBox);
                });
    }
}
