package com.acountservice.service;

import com.acountservice.model.User;

public interface AccountService {

	User saveUserDetails(User user);

	String getToken(User user);

	String validateToken(String token,String username);

}
