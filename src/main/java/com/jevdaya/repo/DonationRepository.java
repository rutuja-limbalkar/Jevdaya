package com.jevdaya.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jevdaya.Entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
	
	List<Donation> findByEmail(String email);
	
	 List<Donation> findAllByOrderByPaymentDateDesc();
	    
	    List<Donation> findByPaymentDateBetweenOrderByPaymentDateDesc(
	        LocalDateTime start, LocalDateTime end);
}
