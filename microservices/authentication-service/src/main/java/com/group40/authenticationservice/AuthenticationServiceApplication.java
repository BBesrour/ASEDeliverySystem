package com.group40.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Base64;


@SpringBootApplication
public class AuthenticationServiceApplication {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SecureRandom getSecureRandom(){
        return new SecureRandom();
    }

    @Bean
    public Base64.Encoder getBase64Encoder(){
        return Base64.getEncoder();
    }
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }
}
