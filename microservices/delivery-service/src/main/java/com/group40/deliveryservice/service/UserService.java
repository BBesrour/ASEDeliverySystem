package com.group40.deliveryservice.service;


import com.group40.deliveryservice.exceptions.UserNotFoundException;
import com.group40.deliveryservice.model.Customer;
import com.group40.deliveryservice.model.ERole;
import com.group40.deliveryservice.model.EmailDetails;
import com.group40.deliveryservice.model.User;
import com.group40.deliveryservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private EmailService emailService;


    public User getUserFromDB(String id){
        return userRepository.findById(id).orElseThrow();
    }

    public List<Customer> getAllCustomers() {
        return userRepository.findByRole("ROLE_CUSTOMER").stream().map(e -> (Customer) e).collect(Collectors.toList());
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
        createInAuth(user, password);
        // sendMail(deliverer);
        return userRepository.insert(user);
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

    private static void createInAuth(User user, String password){
        String response = executePost(bodyBuilder(user.getEmail(), password, user.getRole().toString()));
        //TODO: Change auth service for better response
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
            URL url = new URL("http://api-gateway:8080/api/auth/register");
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

    public User getUser(String token) throws IOException, JSONException {

        URL url = new URL("http://api-gateay:8080/api/auth/current");
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
        //print in String
        System.out.println(response);
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());

        String id = myResponse.getString("id");
        String email = myResponse.getString("email");
        String role = myResponse.getString("role");
        ERole eRole = ERole.valueOf(role);
        return new User(id, email, eRole);
    }

}
