package com.group40.deliveryservice.service;

import com.group40.deliveryservice.exceptions.DeliveryNotFoundException;
import com.group40.deliveryservice.model.*;
import com.group40.deliveryservice.repository.BoxRepository;
import com.group40.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository repository;

    private final UserService userService;

    private final EmailService emailService;
    private final BoxRepository boxRepository;
    private final BoxService boxService;

    public List<Delivery> getDeliveriesForCustomer(String id){
        return repository.findDeliveriesForCustomer(id);
    }

    public List<Delivery> getDeliveriesForDeliverer(String id){
        return repository.findDeliveriesForDeliverer(id);
    }
    public List<Delivery> getAllDeliveries() {
        return repository.findAll();
    }

    public Delivery getSingleDelivery(String id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    public Delivery saveDelivery(Delivery newDelivery) {
        Box box = boxRepository.findById(newDelivery.getTargetBoxID()).orElseThrow(() -> new DeliveryNotFoundException("Box not found"));
        if (Objects.equals(box.getAssignedCustomer(), "") || Objects.equals(box.getAssignedCustomer(), newDelivery.getTargetCustomerID())){
            box.setAssignedCustomer(newDelivery.getTargetCustomerID());
            boxRepository.save(box);
            return repository.save(newDelivery);
        }
        else {
            throw new DeliveryNotFoundException("Box is already assigned to another customer");
        }
    }


    public Delivery replaceDelivery(Delivery newDelivery, String id, String token) throws JSONException, IOException {

        //create or update delivery with id, in that target box all deliveries are assigned to same customer id
        Box box = boxRepository.findById(newDelivery.getTargetBoxID()).orElseThrow(() -> new DeliveryNotFoundException("Box not found"));
        //get delivery with id
        Delivery oldDelivery = repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
        if (Objects.equals(box.getAssignedCustomer(), "") || Objects.equals(box.getAssignedCustomer(), newDelivery.getTargetCustomerID())){

            Delivery replacedDelivery = repository.findById(id)
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

            box.setAssignedCustomer(newDelivery.getTargetCustomerID());
            boxRepository.save(box);

            // 3 case --> box updated, customer updated, both updated
            boxService.updateTargetCustomer(oldDelivery.getTargetBoxID(), oldDelivery.getTargetCustomerID());

            User user = userService.getUserFromDB(newDelivery.getTargetCustomerID(), token);
            EmailDetails emailDetails = new EmailDetails(user.getEmail(),
                    "Delivery updated!",
                    "Delivery updated. Tracking code: " + replacedDelivery.getId());
            boolean status = emailService.sendSimpleMail(emailDetails);
            if (status) {
                log.info("Mail sent successfully for email: " + user.getEmail());
            } else {
                log.error("Mail not sent for email: " + user.getEmail());
            }
            return replacedDelivery;
        } else {
            throw new DeliveryNotFoundException("Box is already assigned to another customer");
        }

        
    }

    public void deleteDelivery(String id) throws Exception {
        //get delivery by id
        Delivery delivery = repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
        repository.deleteById(id);
        boxService.updateTargetCustomer(delivery.getTargetBoxID(), delivery.getTargetCustomerID());

    }

    public List<Delivery> getActiveDeliveries(String customer) {
        return repository.findActiveDeliveries(customer);
    }

    public List<Delivery> getInactiveDeliveries(String customer) {
        return repository.findInactiveDeliveries(customer);
    }

    public List<Delivery> changeDeliveriesInBoxStatus(String boxID, DeliveryStatus status, Boolean active, String token) throws JSONException, IOException {
        // each of status and active can be null to indicate no change
        List<Delivery> toUpdate = repository.findDeliveriesForBox(boxID);
        for (Delivery delivery : toUpdate) {
            if (active != null) {
                delivery.setActive(active);
            }
            if (status != null) {
                delivery.setStatus(status);
            }
            repository.save(delivery);
        }
        User user = userService.getUserFromDB(toUpdate.get(0).getTargetCustomerID(), token);
        String activeMessage = active == null ? "" : "Active status was updated to " + active;
        String statusMessage = status == null ? "" : "Delivery status was updated to " + status;
        EmailDetails emailDetails = new EmailDetails(user.getEmail(),
                "Delivery status for box " + boxID + " was updated: " + activeMessage + " " + statusMessage,
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
