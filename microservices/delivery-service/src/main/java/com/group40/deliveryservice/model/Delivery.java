package com.group40.deliveryservice.model;

import org.springframework.data.annotation.Id;

public class Delivery {
    @Id
    private String id;

    private boolean isActive;
    private String targetCustomerID;
    private String targetBoxID;
    private String delivererID;
    private DeliveryStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTargetCustomerID() {
        return targetCustomerID;
    }

    public void setTargetCustomerID(String targetCustomerID) {
        this.targetCustomerID = targetCustomerID;
    }

    public String getTargetBoxID() {
        return targetBoxID;
    }

    public void setTargetBoxID(String targetBoxID) {
        this.targetBoxID = targetBoxID;
    }

    public String getDelivererID() {
        return delivererID;
    }

    public void setDelivererID(String delivererID) {
        this.delivererID = delivererID;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
