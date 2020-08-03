package com.ssafy.user.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ssafy.user.model.User;
import com.ssafy.user.repository.UserRepository;
import com.ssafy.user.security.service.OAuthService;
import com.ssafy.user.util.JwtUtil;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OAuthService oAuthService;
	
	@Autowired
	private JwtUtil jwtUtil;



	public Optional<User> getUser(int user_id){
		return  userRepository.findById(user_id);
	}
	

	public boolean insertUser(User user){
		return userRepository.save(user) !=null;
	}

	public User updateUser(User user){
		return userRepository.save(user);
	} 
	
	public User getOtherUser(int user_id) {
		Optional<User> oUser = userRepository.findById(user_id);
		
		if(oUser.isEmpty()) return null;
		User user = oUser.get();
		user.setAddress("");
		user.setPointVal(0);
		user.setSocialUid("");
		return user;
	}

}
