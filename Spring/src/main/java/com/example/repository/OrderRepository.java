package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findById(Long Id);
}
