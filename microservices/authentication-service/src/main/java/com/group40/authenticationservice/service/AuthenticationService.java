package com.group40.authenticationservice.service;

import com.group40.authenticationservice.dto.PersonRequest;
import com.group40.authenticationservice.dto.PersonResponse;
import com.group40.authenticationservice.model.Person;
import com.group40.authenticationservice.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    public void createPerson(PersonRequest personRequest) {
        Person person = Person.builder()
                .email(personRequest.getEmail())
                .password(personRequest.getPassword())
                .build();

        authenticationRepository.save(person);
        log.info("Person {} is saved", person.getId());
    }

    public List<PersonResponse> getAllPersons() {
        List<Person> persons = authenticationRepository.findAll();

        return persons.stream().map(this::mapToPersonResponse).toList();
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .email(person.getEmail())
                .password(person.getPassword())
                .build();
    }
}

