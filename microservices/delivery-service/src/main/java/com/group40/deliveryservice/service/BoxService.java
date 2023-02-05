package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.DeliveryStatus;
import com.group40.deliveryservice.repository.BoxRepository;
import com.group40.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoxService {

    private final BoxRepository boxRepository;

    private final DeliveryRepository deliveryRepository;

    private List<Box> getBoxes(){
        return boxRepository.findAll();
    }

    private BoxResponse mapToBoxResponse(Box box) {
        return BoxResponse.builder()
                .id(box.getId())
                .address(box.getAddress())
                .name(box.getName())
                .assignedCustomer(box.getAssignedCustomer())
                .build();
    }

    private void checkBoxNameDuplicate(Box newBox) throws BoxNameDuplicateException {
        Box existingBoxWithSameName = boxRepository.findByName(newBox.getName());
        if (existingBoxWithSameName != null && !existingBoxWithSameName.getId().equals(newBox.getId()))
            throw new BoxNameDuplicateException("Box name already exists! " + newBox.getName());
    }

    public BoxResponse createBox(BoxRequest boxRequest) throws Exception {
        Box box;
        if (Objects.equals(boxRequest.getId(), "")) {
            box = Box.builder()
                    .id(boxRequest.getId())
                    .address(boxRequest.getAddress())
                    .name(boxRequest.getName())
                    .assignedCustomer(boxRequest.getAssignedCustomer())
                    .build();

        } else {
            box = Box.builder()
                    .id(UUID.randomUUID().toString())
                    .address(boxRequest.getAddress())
                    .name(boxRequest.getName())
                    .assignedCustomer(boxRequest.getAssignedCustomer())
                    .build();

        }
        checkBoxNameDuplicate(box);
        boxRepository.insert(box);
        return mapToBoxResponse(box);
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

    private Box updateField(String key, String value, Box box) throws BoxNameDuplicateException {
        switch (key) {
            case "name" -> box.setName(value);
            case "address" -> box.setAddress(value);
            case "assignedCustomer" -> box.setAssignedCustomer(value);
        }
        checkBoxNameDuplicate(box);
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

    public Box replaceBox(Box newBox, String id) throws BoxNameDuplicateException {
        checkBoxNameDuplicate(newBox);
        return boxRepository.findById(id)
                .map(box -> {
                    box.setName(newBox.getName());
                    box.setAddress(newBox.getAddress());
                    box.setAssignedCustomer(newBox.getAssignedCustomer());
                    return boxRepository.save(box);
                })
                .orElseGet(() -> {
                    newBox.setId(id);
                    return boxRepository.save(newBox);
                });
    }

    public List<BoxResponse> getBoxesByDeliverer(String id) {
        List<Delivery> deliveries = deliveryRepository.findAll().stream().filter(delivery -> Objects.equals(delivery.getDelivererID(), id)).toList();

        return getBoxes().stream()
                .filter(box -> deliveries.stream().anyMatch(delivery -> Objects.equals(delivery.getTargetBoxID(), box.getId())))
                .map(this::mapToBoxResponse)
                .toList();
    }

    public List<BoxResponse> getBoxesByCustomer(String id) {
        return getBoxes().stream()
                .filter(box -> box.getAssignedCustomer().contains(id))
                .map(this::mapToBoxResponse)
                .toList();
    }

    public void resetBox(BoxResponse box) {
        Box newBox = Box.builder()
                .id(box.getId())
                .address(box.getAddress())
                .name(box.getName())
                .assignedCustomer("")
                .build();

        try {
            replaceBox(newBox, box.getId());
        } catch (BoxNameDuplicateException e) {
            // should not happen
            e.printStackTrace();
        }
    }

    
    public void deleteBox(String id) {
        boxRepository.deleteById(id);
    }

    public void updateTargetCustomer(String id, String customerID) throws Exception {
        Box box = getBoxes().stream()
                .filter(box1 -> Objects.equals(id, box1.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("Box cannot found"));

        //GET ALL DELIVERIES
        //get all deliveries from deliveryRepository that have the same target box id

        List<Delivery> deliveries = deliveryRepository.findAll().stream().filter(delivery -> Objects.equals(delivery.getTargetBoxID(), id)).toList();
        if (deliveries.stream().anyMatch(delivery -> delivery.getStatus().equals(DeliveryStatus.ORDERED) || delivery.getStatus().equals(DeliveryStatus.PICKED_UP))) {
            return;
        } else {
            box.setAssignedCustomer("");
            boxRepository.save(box);
        }
     
    }
}
