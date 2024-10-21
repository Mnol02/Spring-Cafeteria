package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	Optional<Menu> findById(Long Id);
}
