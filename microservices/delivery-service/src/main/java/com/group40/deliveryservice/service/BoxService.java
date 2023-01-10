package com.group40.deliveryservice.service;

import com.group40.deliveryservice.dto.BoxRequest;
import com.group40.deliveryservice.dto.BoxResponse;
import com.group40.deliveryservice.dto.PersonResponse;
import com.group40.deliveryservice.model.Box;
import com.group40.deliveryservice.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoxService {

    private final BoxRepository boxRepository;

    @Autowired
    private RestTemplate restTemplate;

    private List<Box> getBoxes(){
        return boxRepository.findAll();
    }

    private BoxResponse mapToBoxResponse(Box box) {
        return BoxResponse.builder()
                .id(box.getId())
                .address(box.getAddress())
                .name(box.getName())
                .key(box.getKey())
                .assigned_by(box.getAssigned_by())
                .assigned_to(box.getAssigned_to())
                .assigned_customers(box.getAssigned_customers())
                .build();
    }

    private Box mapFromBoxRequest(BoxRequest boxRequest) {
        return Box.builder()
                .address(boxRequest.getAddress())
                .name(boxRequest.getName())
                .key(boxRequest.getKey())
                .assigned_by(boxRequest.getAssigned_by())
                .assigned_to(boxRequest.getAssigned_to())
                .assigned_customers(boxRequest.getAssigned_customers())
                .build();
    }

    public BoxResponse createBox(BoxRequest boxRequest) {
        Box box = Box.builder()
                .id(UUID.randomUUID().toString())
                .address(boxRequest.getAddress())
                .name(boxRequest.getName())
                .key(boxRequest.getKey())
                .assigned_by(boxRequest.getAssigned_by())
                .assigned_to(boxRequest.getAssigned_to())
                .assigned_customers(boxRequest.getAssigned_customers())
                .build();

        boxRepository.insert(box);
        return mapToBoxResponse(box);
    }

    public List<BoxResponse> getAllBoxes() {
//        List<Box> boxes = deliveryRepository.findAll();

        return getBoxes().stream().map(this::mapToBoxResponse).toList();
    }



    public BoxResponse getBox(String id) throws Exception {
//        List<Box> boxes = deliveryRepository.findAll();

        Box box = getBoxes().stream()
                .filter(box1 -> Objects.equals(id, box1.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("Box cannot found"));
        return mapToBoxResponse(box);
    }

    private Box updateField(String key, String value, Box box) {
        switch(key) {
            case "name":
                box.setName(value);
                break;
            case "key":
                box.setKey(value);
                break;
            case "assigned_to":
                box.setAssigned_to(value);
                break;
            case "assigned_by":
                box.setAssigned_by(value);
                break;
            case "address":
                box.setAddress(value);
                break;
            case "assigned_customers":
                box.getAssigned_customers().add(value);
                break;
        }
        return box;
    }

    public BoxResponse updateBox(String id, Map<String, String> obj) throws Exception {
        for (int i=0; i<getBoxes().size(); i++) {
            Box b = getBoxes().get(i);
            if (Objects.equals(b.getId(), id)) {
                for(String key: obj.keySet()) {
                    b = updateField(key, obj.get(key), b);
                }
                boxRepository.save(b);
                return mapToBoxResponse(b);
            }
        }
        throw new Exception("Box ID does not exist!!!");
    }

    public void deleteBox(String id) throws Exception {
        boxRepository.deleteById(id);
    }


    public PersonResponse getUser(String token) throws IOException, JSONException {

        URL url = new URL("http://localhost:51072/api/auth/current");
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
        System.out.println(response.toString());
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray roles = myResponse.getJSONArray("roles");
        Set<String> resRoles = new HashSet<>();
        for (int i=0; i<roles.length(); i++) {
            resRoles.add(roles.getString(i));
        }

        return PersonResponse.builder()
                .email(myResponse.getString("email"))
                .roles(resRoles)
                .id(myResponse.getString("id"))
                .build();
    }
}
