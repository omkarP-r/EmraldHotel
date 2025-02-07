package com.project.emrald.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.emrald.entity.Room;
import com.project.emrald.entity.User;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

	private Long id;
	private LocalDate checkInDate;
	 private LocalDate checkOutDate;
	 private Integer numOfAdults;
	 private Integer numOfChildren;
	 private Integer totalNumOfGuests;
	 private String bookingConfirmationCode;
	 private UserDTO user;
	 private RoomDTO room;
	
}
