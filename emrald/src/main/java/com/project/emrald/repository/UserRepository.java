package com.project.emrald.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.emrald.entity.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{
	
	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);

}
