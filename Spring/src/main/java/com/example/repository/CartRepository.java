package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByMemberId(Long id);
}
