package com.fake.tinkoff.repository;

import com.fake.tinkoff.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
