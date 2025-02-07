package com.project.emrald.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.emrald.dto.Response;
import com.project.emrald.dto.RoomDTO;
import com.project.emrald.dto.UserDTO;
import com.project.emrald.entity.Room;
import com.project.emrald.entity.User;
import com.project.emrald.exception.OurException;
import com.project.emrald.repository.BookingRepository;
import com.project.emrald.repository.RoomRepository;
import com.project.emrald.service.AwsS3Service;
import com.project.emrald.service.Interface.IRoomService;
import com.project.emrald.utils.Utils;

@Service
public class RoomService implements IRoomService{
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private AwsS3Service awsS3Service;
	@Override
	public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     
			String imageUrl = awsS3Service.saveImageToS3(photo);
			Room room = new Room();
			room.setRoomPhotoUrl(imageUrl);
			room.setRoomType(roomType);
			room.setRoomPrice(roomPrice);
			room.setRoomDescription(description);
			Room savedRoom = roomRepository.save(room);
			RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
			response.setRoom(roomDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error saving a room "+e.getMessage());
		}
		return response;
	}

	@Override
	public List<String> getAllRoomTypes() {
		// TODO Auto-generated method stub
		return roomRepository.findDistinctRoomTypes();
	}

	@Override
	public Response getAllRooms() {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     
			List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));	
			List<RoomDTO> roomListDTO = Utils.mapRoomListEntityToRoomListDTO(roomList);
			response.setRoomlist(roomListDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting all rooms "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response deleteRoom(Long roomId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     
			roomRepository.findById(roomId).orElseThrow(() -> new OurException ("Room Not Found"));
			roomRepository.deleteById(roomId);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}
		catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
		}
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error deleting a room "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice,
			MultipartFile photo) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     
			String imageUrl = null;
			if(photo != null && !photo.isEmpty()) {
				imageUrl = awsS3Service.saveImageToS3(photo);
				
			}
			
			Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Found"));
			if(roomType != null) room.setRoomType(roomType);
			if(roomPrice != null) room.setRoomPrice(roomPrice);
			if(description != null) room.setRoomDescription(description);
			if(imageUrl != null) room.setRoomPhotoUrl(imageUrl);
			
			Room updatedRoom = roomRepository.save(room);
			RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);
			
			response.setRoom(roomDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}
		catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage("Error deleting a room "+oe.getMessage());
		}
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error updating a room "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getRoomById(Long roomId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     
			Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException ("Room Not Found"));
			RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
			response.setRoom(roomDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}
		catch(OurException oe) {
			response.setStatusCode(404);
			response.setMessage(oe.getMessage());
		}
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting a room "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     
			List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndTypes(checkInDate, checkOutDate, roomType);
			List<RoomDTO> availableRoomsDTO = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
			response.setRoomlist(availableRoomsDTO);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}
		
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting room by date and Type a room "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAllAvailableRooms() {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
		     List<Room> availableRooms = roomRepository.getAvailableRooms();
		     List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
		     response.setRoomlist(roomDTOList);
			response.setMessage("Successful");
			response.setStatusCode(200);
			
			
		}
		
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error getting all rooms "+e.getMessage());
		}
		return response;
	}

}
