package com.group40.deliveryservice.model;

public enum DeliveryStatus {
    ORDERED("ORDERED"),
    PICKED_UP("PICKED_UP");

    public final String status;
    DeliveryStatus(String status) {
        this.status = status;
    }
}
