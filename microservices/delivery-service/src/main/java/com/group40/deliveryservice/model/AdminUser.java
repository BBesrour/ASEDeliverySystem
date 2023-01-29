package com.group40.deliveryservice.model;

public class AdminUser extends User {
    public AdminUser() {
        super();
        setId("admin");
        setRole(ERole.ROLE_DISPATCHER);
    }
}
