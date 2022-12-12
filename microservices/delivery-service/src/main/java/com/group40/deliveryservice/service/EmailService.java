package com.group40.deliveryservice.service;

// Importing required classes
import com.group40.deliveryservice.model.EmailDetails;

// Interface
public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

}