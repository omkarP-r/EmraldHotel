package com.project.emrald.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.project.emrald.entity.Booking;

public interface BookingRepository extends CrudRepository<Booking, Long> {
	
	Optional<Booking> findByBookingConfirmationCode( String confirmationCode);

}
