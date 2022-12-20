package com.group40.deliveryservice.service;

import com.group40.deliveryservice.model.EmailDetails;

public interface EmailService {

    // To send a simple email
    boolean sendSimpleMail(EmailDetails details);

}