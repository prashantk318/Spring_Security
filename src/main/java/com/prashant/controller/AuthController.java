package com.prashant.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prashant.entity.Role;
import com.prashant.entity.User;
import com.prashant.payload.JWTAuthResponse;
import com.prashant.payload.LoginDto;
import com.prashant.payload.SignUpDto;
import com.prashant.repository.RoleRepository;
import com.prashant.repository.UserRepository;
import com.prashant.security.JwtTokenprovider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
@Autowired
private AuthenticationManager authenticationManager;

@Autowired
private PasswordEncoder encoder;

@Autowired
JwtTokenprovider jwtTokeProvider;


@PostMapping("/signin")
public ResponseEntity<JWTAuthResponse>authenticateUser(@RequestBody LoginDto loginDto){
Authentication authentication  =	authenticationManager.authenticate
	(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
SecurityContextHolder.getContext().setAuthentication(authentication);
//get token from tokenProvider
String token = jwtTokeProvider.generateToken(authentication);
return  ResponseEntity.ok(new JWTAuthResponse(token));
	
}

@PostMapping("/signup")
public ResponseEntity<?>registerUser(@RequestBody SignUpDto signUpDto){
	//add check for username exists in a DB
	if(userRepository.existsByusername(signUpDto.getUsername())) {
		return new ResponseEntity<String>("userName is Already Taken", HttpStatus.BAD_REQUEST);
	}
	//add check for email exists in a DB
	if(userRepository.existsByEmail(signUpDto.getEmail())) {
		return new ResponseEntity<String>("Email is Already Taken", HttpStatus.BAD_REQUEST);
	}
	
	//create user Object
	User user = new User();
	user.setEmail(signUpDto.getEmail());
	user.setName(signUpDto.getName());
	user.setPassword(encoder.encode(signUpDto.getPassword()));
	user.setUsername(signUpDto.getUsername());
	Role roles = roleRepository.findByName("ROLE_ADMIN").get();
	user.setRoles(Collections.singleton(roles));
	userRepository.save(user);
	return new ResponseEntity<String>("User Registered Successfully", HttpStatus.OK);
	
}

}
