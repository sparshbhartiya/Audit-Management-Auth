package com.cognizant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.model.AuthResponse;
import com.cognizant.model.ProjectManager;
import com.cognizant.repository.ManagerRepository;
import com.cognizant.service.JwtUtil;
import com.cognizant.service.ManagerDetailsService;

import lombok.extern.slf4j.Slf4j;

@RestController

@Slf4j
public class AuthController {

	@Autowired
	private JwtUtil jwtutil;
	@Autowired
	private ManagerDetailsService managerDetailsService;
	@Autowired
	private ManagerRepository repository;
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping(path = "/health")
	public ResponseEntity<?> healthCheckup() {
		log.info("AWS Health Check ");
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody ProjectManager userLoginCredentials) throws Exception{
		log.debug("Start : {}", "login");
		
		final UserDetails userdetails = managerDetailsService.loadUserByUsername(userLoginCredentials.getUserId());
		if (userdetails.getPassword().equals(userLoginCredentials.getPassword())) {
			log.debug("End : {}", "login");
			return new ResponseEntity<>(
					new ProjectManager(userLoginCredentials.getUserId(), userLoginCredentials.getPassword(), jwtutil.generateToken(userdetails)),
					HttpStatus.OK);
		} else {
			log.debug("Access Denied : {}", "login");
			return new ResponseEntity<>(new ProjectManager(), HttpStatus.FORBIDDEN);
		}
	}

		/*
		log.debug("Start : {}", "login");
		System.out.println(userLoginCredentials.toString());
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userLoginCredentials.getUserId(), userLoginCredentials.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		System.out.println("Hellllllloooooo");
		final UserDetails userDetails = managerDetailsService.loadUserByUsername(userLoginCredentials.getUserId());
		System.out.println(userDetails);
			final String jwt = jwtutil.generateToken(userDetails);
			log.debug("End : {}", "login");

			return ResponseEntity.ok(new AuthResponse(jwt));
		*/
	

	
	@GetMapping(value = "/validate")
	public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization") String token) {
		token = token.substring(7);
		AuthResponse res = new AuthResponse();
		log.debug("Start : {}", "getValidity");

		if (jwtutil.validateToken(token)) {
			res.setUid(jwtutil.extractUsername(token));
			res.setValid(true);
			res.setName((repository.findById(jwtutil.extractUsername(token)).orElse(null).getUserId()));

		} else
			res.setValid(false);
		log.debug("End : {}", "getValidity");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
}
