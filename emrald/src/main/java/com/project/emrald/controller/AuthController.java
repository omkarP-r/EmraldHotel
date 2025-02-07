package com.project.emrald.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.emrald.dto.LoginRequest;
import com.project.emrald.dto.Response;
import com.project.emrald.entity.User;
import com.project.emrald.service.Interface.IUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private IUserService iUserService;
	
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody User user){
		Response response = iUserService.register(user);
		return ResponseEntity.status(response.getStatusCode()).body(response);
		}
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
		Response response = iUserService.login(loginRequest);
		return ResponseEntity.status(response.getStatusCode()).body(response);
		}
}
