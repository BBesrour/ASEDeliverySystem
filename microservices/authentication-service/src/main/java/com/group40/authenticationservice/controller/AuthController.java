package com.group40.authenticationservice.controller;

import com.group40.authenticationservice.dto.request.PersonRequest;
import com.group40.authenticationservice.dto.request.SignupRequest;
import com.group40.authenticationservice.dto.response.JwtResponse;
import com.group40.authenticationservice.dto.response.MessageResponse;
import com.group40.authenticationservice.dto.response.PersonResponse;
import com.group40.authenticationservice.model.ERole;
import com.group40.authenticationservice.model.User;
import com.group40.authenticationservice.model.Role;
import com.group40.authenticationservice.repository.RoleRepository;
import com.group40.authenticationservice.repository.UserRepository;
import com.group40.authenticationservice.security.jwt.JwtUtils;
import com.group40.authenticationservice.security.services.UserDetailsImpl;
import com.group40.authenticationservice.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;


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

	@Autowired
	private SecureRandom secureRandom;
	@Autowired
	private Base64.Encoder base64Encoder;

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
	@PreAuthorize("hasRole('DISPATCHER')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Id/Email is already in use!"));
		}
		if (signUpRequest.getPassword().isBlank()){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No Password found"));
		}

		byte[] randomBytes = new byte[12];
		secureRandom.nextBytes(randomBytes);

		// Create new user's account
		User user = User.builder()
				.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.token(base64Encoder.encodeToString(randomBytes))
				.build();

		String strRole = signUpRequest.getRole();
		Role role;

		if (strRole == null || strRole.isBlank()) {
			role = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		} else {
			role = roleRepository.findByName(ERole.valueOf(strRole.toUpperCase()))
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		}

		user.setRole(role);
		userRepository.save(user);
		return ResponseEntity.ok(new PersonResponse(user.getId(), user.getEmail(), role.getName().name(), user.getToken()));
	}

	@GetMapping("/current")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('DISPATCHER') or hasRole('DELIVERER')")
	public ResponseEntity<?> userAccess() {
		UserDetailsImpl userDetails =
				(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String role = userDetails.getAuthority().getAuthority();

		return ResponseEntity.ok(new PersonResponse(userDetails.getId(), userDetails.getUsername(), role, userDetails.getToken()));
	}

	@RequestMapping("/csrf")
	public CsrfToken csrf(CsrfToken token) {
		return token;
	}



}
