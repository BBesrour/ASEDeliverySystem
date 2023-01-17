package com.group40.authenticationservice.controller;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.group40.authenticationservice.repository.RoleRepository;
import com.group40.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.group40.authenticationservice.model.ERole;
import com.group40.authenticationservice.model.Role;
import com.group40.authenticationservice.model.Person;
import com.group40.authenticationservice.dto.request.PersonRequest;
import com.group40.authenticationservice.dto.response.PersonResponse;
import com.group40.authenticationservice.dto.request.SignupRequest;
import com.group40.authenticationservice.dto.response.JwtResponse;
import com.group40.authenticationservice.dto.response.MessageResponse;
import com.group40.authenticationservice.security.jwt.JwtUtils;
import com.group40.authenticationservice.security.services.UserDetailsImpl;

import com.group40.authenticationservice.security.services.UserDetailsServiceImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody PersonRequest personRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(personRequest.getEmail(), personRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		String role = userDetails.getAuthority().getAuthority();

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(),
												 userDetails.getEmail(),
												 role));
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		Person user = Person.builder()
				.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.build();

		String strRole = signUpRequest.getRole();
		Role role;

		if (strRole == null || strRole.isBlank()) {
			role = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		} else {
				switch (strRole) {
					case "dispatcher" -> role = roleRepository.findByName(ERole.ROLE_DISPATCHER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					case "deliverer" -> role = roleRepository.findByName(ERole.ROLE_DELIVERER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					default -> role = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				}

		}

		user.setRole(role);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@GetMapping("/current")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('DISPATCHER') or hasRole('DELIVERER')")
	public ResponseEntity<?> userAccess() {
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String role = userDetails.getAuthority().getAuthority();

		return ResponseEntity.ok(new PersonResponse(userDetails.getId(), userDetails.getUsername(), role));
	}


}
