package com.group40.deliveryservice.service;


import com.group40.deliveryservice.exceptions.UserNotFoundException;
import com.group40.deliveryservice.model.*;
import com.group40.deliveryservice.repository.CustomerRepository;
import com.group40.deliveryservice.repository.DelivererRepository;
import com.group40.deliveryservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final DelivererRepository delivererRepository;

    private final CustomerRepository customerRepository;

    private final EmailService emailService;


    @Value("${application.apiUrl}")
    private String apiURL;

    @Value("${adminToken}")
    private String adminToken;

    public boolean adminTokenIsValid(String token) {
        return token.equals("Bearer " + adminToken);
    }


    public User getUserFromDB(String id) throws IOException, JSONException {
        if (id.equals("admin")) {
            return new AdminUser();
        }
        URL url = new URL(apiURL + "/api/auth/user/" + id);
        return executeGetUser(url, "Bearer " + adminToken);
    }

    public Deliverer getDelivererFromDB(String email){
        return delivererRepository.findByEmail(email);
    }

    public Customer getCustomerFromDB(String email){
        return customerRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        //get all users from user
        List<User> users = userRepository.findAll();
        // get all users from deliverer and customer
        List<Deliverer> deliverers = delivererRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        // replace the users with the deliverers and customers by id
        users.replaceAll(user -> {
            if (user.getRole().equals(ERole.ROLE_DELIVERER)) {
                return deliverers.stream().filter(deliverer -> deliverer.getId().equals(user.getId())).findFirst().orElse((Deliverer) user);
            } else if (user.getRole().equals(ERole.ROLE_CUSTOMER)) {
                return customers.stream().filter(customer -> customer.getId().equals(user.getId())).findFirst().orElse((Customer) user);
            }
            return user;
        });
        return users;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User newUser, String id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(User user, String password) {
        User createdUser = userRepository.insert(user);
        createInAuth(createdUser, password);
        // sendMail(deliverer);
        return createdUser;
    }

    public Deliverer createDeliverer(Deliverer deliverer, String password){
        Deliverer createdUser = delivererRepository.insert(deliverer);
        createInAuth(createdUser, password);
        // sendMail(deliverer);
        return createdUser;
    }

    public Customer createCustomer(Customer customer, String password){
        Customer createdUser = customerRepository.insert(customer);
        createInAuth(createdUser, password);
        // sendMail(deliverer);
        return createdUser;
    }

    private void sendMail(User user){
        EmailDetails emailDetails = new EmailDetails(user.getEmail(),
                "A new account was create for you. Role: " + user.getRole(),
                "ASE Delivery: new account");
        boolean status = emailService.sendSimpleMail(emailDetails);
        if (status) {
            log.info("Mail sent successfully for email: " + user.getEmail());
        } else {
            log.error("Mail not sent for email: " + user.getEmail());
        }
    }

    private void createInAuth(User user, String password){
        String response = executePost(bodyBuilder(user.getId(), user.getEmail(), password, user.getRole().toString()));
        //TODO: Change auth service for better response
        if (!response.contains("User registered successfully!")){
            throw new RuntimeException("Couldn't create user in auth service");
        }
    }

    private static String bodyBuilder(String id, String email, String password, String role) {
        return """
                {
                    "id": "%s",
                    "email": "%s",
                    "password": "%s",
                    "role": "%s"
                }
                """.formatted(id, email, password, role);
    }

    private static String getCSRF() throws IOException, JSONException {
        URL url = new URL("http://localhost:8080/api/auth/csrf");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        System.out.println(response);
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());

        return myResponse.getString("token");

    }

    private String executePost(String body) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            String csrf = getCSRF();
            System.out.println(csrf);
            URL url = new URL(apiURL + "/api/auth/register");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-XSRF-TOKEN", csrf);
            connection.setRequestProperty("Cookie", "XSRF-TOKEN="+csrf);
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

    public User getUser(String token) throws IOException, JSONException {
        if (adminTokenIsValid(token)) {
            return new AdminUser();
        }

        URL url = new URL(apiURL + "/api/auth/current");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        return executeGetUser(url, token);
    }

    public User getUserFromToken(String token) throws IOException, JSONException {
        if (adminTokenIsValid(token)) {
            return new AdminUser();
        }
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        URL url = new URL(apiURL + "/api/auth/user/token-to-user?token="+ encodedToken);
        return executeGetUser(url, token);
    }

    public User executeGetUser(URL url, String token) throws IOException, JSONException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", token);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Language", "en-US");
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject myResponse = new JSONObject(response.toString());

        String id = myResponse.getString("id");
        String email = myResponse.getString("email");
        String role = myResponse.getString("role");
        ERole eRole = ERole.valueOf(role);
        return new User(id, email, eRole);
    }

}
