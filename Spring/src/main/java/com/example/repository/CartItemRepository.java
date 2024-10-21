package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findById(Long id);
}
