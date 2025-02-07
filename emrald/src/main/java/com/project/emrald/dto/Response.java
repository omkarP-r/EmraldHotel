package com.project.emrald.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

	
	private Integer statusCode;
	private String message;
	private String token;
	private String role;
	private String expirationTime;
	private String bookingConfirmationCode;
	
	
	private UserDTO user;
	private RoomDTO room;
	private BookingDTO booking;
	
	private List<UserDTO> userlist;
	private List<RoomDTO> roomlist;
	private List<BookingDTO> bookinglist;
	
	
}
