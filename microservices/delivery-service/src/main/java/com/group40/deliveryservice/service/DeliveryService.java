package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public void createBox(BoxRequest boxRequest) {
        Box box = Box.builder()
                .address(boxRequest.getAddress())
                .status(boxRequest.getStatus())
                .name(boxRequest.getName())
                .number_of_items(boxRequest.getNumber_of_items())
                .build();

        deliveryRepository.save(box);
        log.info("box {} is saved", box.getId());
    }

    public List<BoxResponse> getAllBoxes() {
        List<Box> boxes = deliveryRepository.findAll();

        return boxes.stream().map(this::mapToBoxResponse).toList();
    }

    private BoxResponse mapToBoxResponse(Box box) {
        return BoxResponse.builder()
                .id(box.getId())
                .address(box.getAddress())
                .status(box.getStatus())
                .name(box.getName())
                .number_of_items(box.getNumber_of_items())
                .build();
    }
}