package com.project.emrald.service.Interface;

import com.project.emrald.dto.LoginRequest;
import com.project.emrald.dto.Response;
import com.project.emrald.entity.User;

public interface IUserService {

	
	Response register (User user);
	
	Response login(LoginRequest loginRequest);
	
	Response getAllUsers();
	
	Response getUserBookingHistory(String userId);
	
	Response getUserById(String userId);
	
	Response getMyInfo(String email);
	
	Response deleteUser(String userId);
}
