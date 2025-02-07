package com.project.emrald.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.emrald.dto.LoginRequest;
import com.project.emrald.dto.Response;
import com.project.emrald.dto.UserDTO;
import com.project.emrald.entity.User;
import com.project.emrald.exception.OurException;
import com.project.emrald.repository.UserRepository;
import com.project.emrald.service.Interface.IUserService;
import com.project.emrald.utils.JWTUtils;
import com.project.emrald.utils.Utils;


@Service
public class UserService implements IUserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
    public Response register(User user) {
        Response response = new Response();

        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                throw new OurException("Password cannot be null or empty");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " " + "Already Exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

            response.setStatusCode(200);
            response.setUser(userDTO);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a User" + e.getMessage());

        }
        return response;
    }


	@Override
	public Response login(LoginRequest loginRequest) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
			authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new OurException("User Not Found"));
			var token= jwtUtils.generateToken(user);
			response.setToken(token);
			response.setExpirationTime("7 days");
			response.setRole(user.getRole());
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error Logging in "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAllUsers() {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
			List<User> userList = userRepository.findAll();
			List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
			response.setUserlist(userDTOList);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error in fectching all users "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getUserBookingHistory(String userId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		
			User user = userRepository
					.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
			UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRooms(user);
			
			
			response.setUser(userDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting in user booking "+e.getMessage());
		}
		return response;
	}


	@Override
	public Response deleteUser(String userId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		
			 userRepository
					.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
			userRepository.deleteById(Long.valueOf(userId));
			
			
			
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error deleting a user "+e.getMessage());
		}
		return response;
	}
	

	@Override
	public Response getUserById(String userId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		
			 User user = userRepository
					.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
			UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
			
			
			
			
			response.setUser(userDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting  a user by ID "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getMyInfo(String email) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		
			 User user = userRepository
					.findByEmail(email).orElseThrow(()-> new OurException("User Not Found"));
			UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
			
			
			
			
			response.setUser(userDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting  a user by ID "+e.getMessage());
		}
		return response;
	}

}
