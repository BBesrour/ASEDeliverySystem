package com.group40.deliveryservice.service;

import com.group40.deliveryservice.exceptions.DeliveryNotFoundException;
import com.group40.deliveryservice.model.Delivery;
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

    private final EmailService emailService;

    private final UserService userService;

    public List<Delivery> getDeliveriesForCustomer(String id){
        return repository.findDeliveriesForCustomer(id);
    }
    public List<Delivery> getAllDeliveries() {
        return repository.findAll();
    }

    public Delivery saveDelivery(Delivery newDelivery) {
        Delivery savedDelivery = repository.save(newDelivery);
        User user = userService.getUserFromDB(newDelivery.getTargetCustomerID());
        EmailDetails emailDetails = new EmailDetails(user.getEmail(),
                "A new delivery was created! Tracking code: " + savedDelivery.getId(),
                "New delivery");
        boolean status = emailService.sendSimpleMail(emailDetails);
        if (status) {
            log.info("Mail sent successfully for email: " + user.getEmail());
        } else {
            log.error("Mail not sent for email: " + user.getEmail());
        }
        return savedDelivery;
    }

    public Delivery getSingleDelivery(String id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    public Delivery replaceDelivery(Delivery newDelivery, String id) {
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
