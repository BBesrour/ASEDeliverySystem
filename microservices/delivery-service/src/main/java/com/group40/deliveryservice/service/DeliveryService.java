package com.group40.deliveryservice.service;

import com.group40.deliveryservice.exceptions.DeliveryNotFoundException;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository repository;

    public List<Delivery> getAllDeliveries() {
        return repository.findAll();
    }

    public Delivery saveDelivery(Delivery newDelivery) {
        return repository.save(newDelivery);
    }

    public Delivery getSingleDelivery(String id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    public Delivery replaceDelivery(Delivery newDelivery, String id) {
        return repository.findById(id)
                .map(delivery -> {
                    delivery.setActive(newDelivery.isActive());
                    delivery.setTargetCustomerID(newDelivery.getTargetCustomerID());
                    delivery.setTargetBoxID(newDelivery.getTargetBoxID());
                    delivery.setDelivererID(newDelivery.getDelivererID());
                    return repository.save(delivery);
                })
                .orElseGet(() -> {
                    newDelivery.setId(id);
                    return repository.save(newDelivery);
                });
    }

    public void deleteDelivery(String id) {
        repository.deleteById(id);
    }

    public List<Delivery> getActiveDeliveries(String customer) {
        return repository.findActiveDeliveries(customer);
    }

    public List<Delivery> getInactiveDeliveries(String customer) {
        return repository.findInactiveDeliveries(customer);
    }
}
