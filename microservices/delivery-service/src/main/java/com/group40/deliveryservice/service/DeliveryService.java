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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private List<Box> getBoxes(){
        List<Box> boxes = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            String ist = Integer.toString(i);
            String arr[] = {"1", "2"};
            boxes.add(new Box(ist, "box"+ist, "box"+ist+" adress", "deliverer"+ist, "dispatcher"+ist, arr, ist+ist  ));
        }
        return boxes;
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
                .address(boxRequest.getAddress())
                .name(boxRequest.getName())
                .key(boxRequest.getKey())
                .assigned_by(boxRequest.getAssigned_by())
                .assigned_to(boxRequest.getAssigned_to())
                .assigned_customers(boxRequest.getAssigned_customers())
                .build();

        deliveryRepository.save(box);
        log.info("box {} is saved", box.getId());
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

    public void updateBox(BoxRequest boxRequest) throws Exception {
        for (int i=0; i<getBoxes().size(); i++) {
            if (Objects.equals(getBoxes().get(i).getId(), boxRequest.getId())) {
                getBoxes().set(i, mapFromBoxRequest(boxRequest));
            }
        }
        throw new Exception("Box ID does not exist!!!");
    }
}