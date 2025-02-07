package com.project.emrald.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.emrald.dto.Response;
import com.project.emrald.service.Interface.IUserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private IUserService iUserService;
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getAllUsers(){
		Response response = iUserService.getAllUsers();
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping("/get-by-id/{userId}")
	public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId){
		Response response = iUserService.getUserById(userId);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@DeleteMapping("/delete/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId){
		Response response = iUserService.deleteUser(userId);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	
	@GetMapping("/get-logged-in-profile-info")
	public ResponseEntity<Response> getLoggedInUserProfile(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Response response = iUserService.getMyInfo(email);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping("/get-user-bookings/{userId}")
	public ResponseEntity<Response> getUserBookingHistory(@PathVariable("userId") String userId){
		Response response = iUserService.getUserBookingHistory(userId);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	
	
	
}
