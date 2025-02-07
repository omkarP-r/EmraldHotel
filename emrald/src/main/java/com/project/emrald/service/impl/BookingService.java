package com.project.emrald.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.emrald.dto.BookingDTO;
import com.project.emrald.dto.Response;
import com.project.emrald.dto.RoomDTO;
import com.project.emrald.entity.Booking;
import com.project.emrald.entity.Room;
import com.project.emrald.entity.User;
import com.project.emrald.exception.OurException;
import com.project.emrald.repository.BookingRepository;
import com.project.emrald.repository.RoomRepository;
import com.project.emrald.repository.UserRepository;
import com.project.emrald.service.Interface.IBookingService;
import com.project.emrald.service.Interface.IRoomService;
import com.project.emrald.utils.Utils;

@Service
public class BookingService implements IBookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IRoomService iRoomService;
	
	

	@Override
	public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		   if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			   throw new IllegalArgumentException("Check In Date must be before CheckOut Date");
		   }
		   
		   Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Found"));
		   User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found"));
		   
		   List<Booking> existingBooking = room.getBookings();
		   if(!roomIsAvailable( bookingRequest, existingBooking)) {
			   throw new OurException("Room not avaialble for selected Dates Range");
		   }
		   bookingRequest.setRoom(room);
		   bookingRequest.setUser(user);
		   String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
		   bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
		    bookingRepository.save(bookingRequest);
		    
		    response.setBookingConfirmationCode(bookingConfirmationCode);
		   
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
		}
		
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error saving a booking "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response findBookingByConfirmationCode(String confirmationCode) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		    Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
		    		.orElseThrow(() -> new OurException("Booking Not Found"));
		    BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
		    response.setBooking(bookingDTO);
		    
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
		}
		
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting bookings by confirmation code "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAllBookings() {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		    List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		    List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookings);
		    response.setBookinglist(bookingDTOList);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}
		
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting all Bookings "+e.getMessage());
		}
		return response;
		
	}

	@Override
	public Response cancelBooking(Long bookingId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		
			Booking booking = bookingRepository.findById(bookingId)
					.orElseThrow(() -> new OurException("Booking Not Found"));
			bookingRepository.deleteById(bookingId);
			
			
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
		}
		
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error cancelling a booking "+e.getMessage());
		}
		return response;
	}
	
	private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBooking) {
		
		return existingBooking.stream()
				.noneMatch( existingBookings ->
				bookingRequest.getCheckInDate().equals(existingBookings.getCheckInDate()) 
				|| bookingRequest.getCheckOutDate().isBefore(existingBookings.getCheckInDate())
				|| bookingRequest.getCheckInDate().isAfter(existingBookings.getCheckInDate())
				&& bookingRequest.getCheckInDate().isBefore(existingBookings.getCheckOutDate())
				|| bookingRequest.getCheckInDate().isBefore(existingBookings.getCheckInDate())
				
				
				&& bookingRequest.getCheckOutDate().equals(existingBookings.getCheckOutDate())
				|| bookingRequest.getCheckInDate().isBefore(existingBookings.getCheckInDate())
				
				
				&& bookingRequest.getCheckOutDate().isAfter(existingBookings.getCheckOutDate())
				
				|| bookingRequest.getCheckInDate().equals(existingBookings.getCheckOutDate())
				&& bookingRequest.getCheckOutDate().equals(existingBookings.getCheckInDate())
				
				|| bookingRequest.getCheckInDate().equals(existingBookings.getCheckOutDate())
				&& bookingRequest.getCheckOutDate().equals(existingBookings.getCheckInDate())
				
						);
	}

	
}
