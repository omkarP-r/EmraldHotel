package com.project.emrald.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.emrald.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	Optional<Booking> findByBookingConfirmationCode( String confirmationCode);

}
