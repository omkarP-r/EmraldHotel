package com.project.emrald.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.emrald.dto.Response;
import com.project.emrald.entity.Booking;
import com.project.emrald.service.Interface.IBookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	@Autowired
	private IBookingService iBookingService;
	
	

	@PostMapping("/book-room/{roomId}/{userId}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Response> saveBooking(
			@PathVariable Long roomId,
			@PathVariable Long userId,
			@RequestBody Booking bookingRequest
			
			) {

		
		Response response = iBookingService.saveBooking(roomId, userId, bookingRequest);
		
		return ResponseEntity.status(response.getStatusCode()).body(response);

	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getAllBookings() {
		Response response = iBookingService.getAllBookings();
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping("/get-by-confirmation-code/{confirmationCode}")
	public ResponseEntity<Response> getBookingsByConfirmationCode( @PathVariable String confirmationCode) {
		Response response = iBookingService.findBookingByConfirmationCode(confirmationCode);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@DeleteMapping("/delete/{bookingId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> cancelBooking (@PathVariable Long bookingId) {
		Response response = iBookingService.cancelBooking(bookingId);
		return ResponseEntity.status(response.getStatusCode()).body(response);

	}
	
	
}
