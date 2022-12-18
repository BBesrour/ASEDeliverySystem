package com.group40.deliveryservice.service;


import com.group40.deliveryservice.exceptions.UserNotFoundException;
import com.group40.deliveryservice.model.Customer;
import com.group40.deliveryservice.model.Deliverer;
import com.group40.deliveryservice.model.Dispatcher;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<Customer> getAllCustomers() {
        return userRepository.findByRole("ROLE_CUSTOMER").stream().map(e -> (Customer) e).collect(Collectors.toList());
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

/*    public User updateUser(User newUser, String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }*/

    public User updateCustomer(Customer newUser, String id) {
        return userRepository.findById(id).map(user -> {
            Customer customer = (Customer) user;
            customer.setEmail(newUser.getEmail());
            customer.setBoxes(newUser.getBoxes());
            customer.setDeliveries(newUser.getDeliveries());
            return userRepository.save(customer);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateDeliverer(Deliverer newUser, String id) {
        return userRepository.findById(id).map(user -> {
            Deliverer deliverer = (Deliverer) user;
            deliverer.setEmail(newUser.getEmail());
            deliverer.setDeliveries(newUser.getDeliveries());
            return userRepository.save(deliverer);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateDispatcher(Dispatcher newUser, String id) {
        return userRepository.findById(id).map(user -> {
            Dispatcher dispatcher = (Dispatcher) user;
            dispatcher.setEmail(newUser.getEmail());
            return userRepository.save(dispatcher);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

/*    public User createUser(User newUser){
        switch (newUser.getRole()){
            case ROLE_CUSTOMER -> {
                Customer customer = Customer.builder().email(newUser.getEmail()).role(newUser.getRole()).build();
                return userRepository.insert(customer);
            }
            case ROLE_DELIVERER -> {
                Deliverer deliverer = Deliverer.builder().email(newUser.getEmail()).role(newUser.getRole()).build();
                return userRepository.insert(deliverer);
            }
            case ROLE_DISPATCHER -> {
                Dispatcher dispatcher = Dispatcher.builder().email(newUser.getEmail()).role(newUser.getRole()).build();
                return userRepository.insert(dispatcher);
            }
            default -> {
                return userRepository.save(newUser);
            }
        }
    }*/

    public User createCustomer(Customer customer, String password) {
        createInAuth(customer, password);
        return userRepository.insert(customer);
    }

    public User createDeliverer(Deliverer deliverer, String password) {
        createInAuth(deliverer, password);
        return userRepository.insert(deliverer);
    }

    public User createDispatcher(Dispatcher dispatcher, String password) {
        createInAuth(dispatcher, password);
        return userRepository.insert(dispatcher);
    }

    private static void createInAuth(User user, String password){
        String response = executePost(bodyBuilder(user.getEmail(), password, user.getRole().toString()));
        if (!response.contains("User registered successfully!")){
            throw new RuntimeException("Couldn't create user in auth service");
        }
    }

    private static String bodyBuilder(String email, String password, String role) {
        return """
                {
                    "email": "%s",
                    "password": "%s",
                    "role": "%s"
                            
                }
                """.formatted(email, password, role);
    }


    private static String executePost(String body) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            //TODO: Change URL
            URL url = new URL("http://localhost:8080/api/auth/register");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            //Send request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            //Get Response
            String response;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder responseBuilder = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    responseBuilder.append(responseLine.trim());
                }
                response = responseBuilder.toString();
            }
            return response;
        } catch (MalformedURLException e) {
            log.error("Bad URL", e);
            e.printStackTrace();
            throw new RuntimeException("BAD URL");
        } catch (IOException e) {
            log.error("Error when opening URL connection", e);
            e.printStackTrace();
            throw new RuntimeException("Couldn't open connection to URL");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Oopsie!");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
