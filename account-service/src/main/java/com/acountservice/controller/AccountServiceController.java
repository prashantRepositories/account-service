package com.acountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.acountservice.costants.Constants;
import com.acountservice.endpointreferrer.EndPointReferrer;
import com.acountservice.model.User;
import com.acountservice.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountServiceController {
	
	private final Logger logger = LoggerFactory.getLogger(AccountServiceController.class);
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	//Customer Registration
	@PostMapping(EndPointReferrer.CUSTOMER_REGISTRATION)
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		logger.debug(Constants.CONTROLLER_STARTED, EndPointReferrer.CUSTOMER_REGISTRATION);		
		return new ResponseEntity<>(accountService.saveUserDetails(user), HttpStatus.OK) ;
	}	
	//Customer Login
	@PostMapping(EndPointReferrer.CUSTOMER_LOGIN)
	public ResponseEntity<String> loginUser(@RequestBody User user) {
		
		logger.debug(Constants.CONTROLLER_STARTED, EndPointReferrer.CUSTOMER_LOGIN);			
		Authentication auth = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getPassword()));
		if(auth.isAuthenticated()) {
			//Will generate token
			return new ResponseEntity<>(accountService.getToken(user), HttpStatus.CREATED);
		}	
		return new ResponseEntity<>("You logged in successfully!!!", HttpStatus.ACCEPTED);
		
		
	}
	//Token Validation
	@GetMapping(EndPointReferrer.VALIDATION)
	public ResponseEntity<String> validateToken(@RequestParam("token") String token,@RequestParam("userName") String username ) {
		
		logger.debug(Constants.CONTROLLER_STARTED, EndPointReferrer.VALIDATION);
		return new ResponseEntity<>(accountService.validateToken(token,username),HttpStatus.OK);
	}

}
