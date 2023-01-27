package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.exceptions.DeliveryNotFoundException;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.DeliveryStatus;
import com.group40.deliveryservice.model.EmailDetails;
import com.group40.deliveryservice.model.User;
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

    private UserService userService;
    private BoxService boxService;

    private EmailService emailService;

    public List<Delivery> getDeliveriesForCustomer(String id){
        return repository.findDeliveriesForCustomer(id);
    }
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
                    delivery.setStatus(newDelivery.getStatus());
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

    public List<Delivery> changeDeliveriesInBoxStatus(String boxID, DeliveryStatus status) throws Exception {
        List<Delivery> toUpdate = repository.findDeliveriesForBox(boxID);
        for (Delivery delivery : toUpdate) {
            delivery.setStatus(status);
            repository.save(delivery);
        }
        BoxResponse box = boxService.getBox(boxID);
        User user = userService.getUserFromDB(box.getAssignedCustomer());
        EmailDetails emailDetails = new EmailDetails(user.getEmail(),
                "Delivery status for box " + boxID + " was updated to " + status,
                "ASE Delivery: Delivery status");
        boolean mailStatus = emailService.sendSimpleMail(emailDetails);
        if (mailStatus) {
            log.info("Mail sent successfully for email: " + user.getEmail());
        } else {
            log.error("Mail not sent for email: " + user.getEmail());
        }
        return toUpdate;
    }
}
