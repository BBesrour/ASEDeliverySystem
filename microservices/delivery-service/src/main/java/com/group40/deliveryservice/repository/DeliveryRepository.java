package com.group40.deliveryservice.repository;

import com.group40.deliveryservice.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    @Query("{ 'isActive' :  true, 'targetCustomerID' : ?0 }")
    List<Delivery> findActiveDeliveries(String customer);

    @Query("{ 'isActive' :  false, 'targetCustomerID' : ?0 }")
    List<Delivery> findInactiveDeliveries(String customer);

    @Query("{'targetCustomerID' : ?0 }")
    List<Delivery> findDeliveriesForCustomer(String customer);

    @Query("{'delivererID' : ?0 }")
    List<Delivery> findDeliveriesForDeliverer(String deliverer);

    @Query("{'targetBoxID' : ?0 }")
    List<Delivery> findDeliveriesForBox(String boxID);
}
