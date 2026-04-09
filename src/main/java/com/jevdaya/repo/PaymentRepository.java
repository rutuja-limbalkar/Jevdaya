package com.jevdaya.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jevdaya.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
