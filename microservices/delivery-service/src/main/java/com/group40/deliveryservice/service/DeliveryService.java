package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.exceptions.DeliveryNotFoundException;
import com.group40.deliveryservice.model.Delivery;
import com.group40.deliveryservice.model.DeliveryStatus;
import com.group40.deliveryservice.repository.BoxRepository;
import com.group40.deliveryservice.model.EmailDetails;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import com.group40.deliveryservice.model.Box;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository repository;

    private final UserService userService;

    private final EmailService emailService;
    private final BoxRepository boxRepository;

    public List<Delivery> getDeliveriesForCustomer(String id){
        return repository.findDeliveriesForCustomer(id);
    }
    public List<Delivery> getAllDeliveries() {
        return repository.findAll();
    }

    public Delivery saveDelivery(Delivery newDelivery) {

        Box box = boxRepository.findById(newDelivery.getTargetBoxID()).orElseThrow(() -> new DeliveryNotFoundException("Box not found"));
        if (Objects.equals(box.getAssignedCustomer(), "") || Objects.equals(box.getAssignedCustomer(), newDelivery.getTargetCustomerID())){
            box.setAssignedCustomer(newDelivery.getTargetCustomerID());
            boxRepository.save(box);
            return repository.save(newDelivery);
        }
        else{
            throw new DeliveryNotFoundException("Box is already assigned to another customer");
        }
    }

    public Delivery getSingleDelivery(String id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    public Delivery replaceDelivery(Delivery newDelivery, String id) {

        //create or update delivery with id, in that target box all deliveries are assigned to same customer id
        Box box = boxRepository.findById(newDelivery.getTargetBoxID()).orElseThrow(() -> new DeliveryNotFoundException("Box not found"));
        if (Objects.equals(box.getAssignedCustomer(), "") || Objects.equals(box.getAssignedCustomer(), newDelivery.getTargetCustomerID())){
            box.setAssignedCustomer(newDelivery.getTargetCustomerID());
            boxRepository.save(box);

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
            User user = userService.getUserFromDB(newDelivery.getTargetCustomerID());
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
        }
        else{
            throw new DeliveryNotFoundException("Box is already assigned to another customer");
        }

        
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
        Box box = boxRepository.findById(boxID).orElseThrow(() -> new Error("Box Not Found"));
        box.setAssignedCustomer("");
        boxRepository.save(box);
    
        User user = userService.getUserFromDB(toUpdate.get(0).getTargetCustomerID());
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
