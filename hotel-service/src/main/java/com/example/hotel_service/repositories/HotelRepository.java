package com.example.hotel_service.repositories;

import com.example.hotel_service.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    Optional<Hotel> findByName(String name);
}
