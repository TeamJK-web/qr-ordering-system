package com.restaurant.ordering.repository;
import com.restaurant.ordering.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
