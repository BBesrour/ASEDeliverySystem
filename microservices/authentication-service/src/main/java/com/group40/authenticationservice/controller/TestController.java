package com.group40.authenticationservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('DISPATCHER') or hasRole('DELIVERER')")
	public String userAccess() {
		return "User.";
	}

	@GetMapping("/dispatcher")
	@PreAuthorize("hasRole('DISPATCHER')")
	public String moderatorAccess() {
		return "dispatcher.";
	}

	@GetMapping("/deliverer")
	@PreAuthorize("hasRole('DELIVERER')")
	public String adminAccess() {
		return "deliverer.";
	}
}
