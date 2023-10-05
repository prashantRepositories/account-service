package com.acountservice.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acountservice.costants.Constants;
import com.acountservice.dao.AccountServiceRepository;
import com.acountservice.endpointreferrer.EndPointReferrer;
import com.acountservice.model.User;

@Service
public class AccountServiceImp implements AccountService,UserDetailsService{
	
	private final Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);
	
	@Autowired
	AccountServiceRepository accountServiceRepository;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtils jwtUtils;

	@Override
	public User saveUserDetails(User user) {
		
		String encodedPass = encodePassword(user.getPassword());
		user.setPassword(encodedPass);
		return accountServiceRepository.save(user);
	}

	
	private String encodePassword(String password) {
		String encodedPasswprd = passwordEncoder.encode(password);
		return encodedPasswprd;
	}


	@Override
	public String getToken(User user) {
		
		return JwtUtils.generateToken(user);
	}


	@Override
	public String validateToken(String token, String username) {
		// TODO Auto-generated method stub
		if (jwtUtils.validateToken(token, username)) {
			return "You are authenticated";
		}
		logger.error("Invalid token [{}]", Constants.INVALID_TOKEN);	
		return "Your token is not valid";
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = accountServiceRepository.findByUserEmail(username);
		
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getPassword(), true, true, true, true, new ArrayList<>());
		
		return userDetails;
	}
}
