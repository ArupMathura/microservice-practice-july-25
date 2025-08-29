package com.example.hotel_service.services.impl;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.exceptions.ResourceNotFoundException;
import com.example.hotel_service.repositories.HotelRepository;
import com.example.hotel_service.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    private HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Hotel create(Hotel hotel) {
        Optional<Hotel> hotelRepositoryById = hotelRepository.findByName(hotel.getName());
        if (hotelRepositoryById.isPresent()) {
            throw new ResourceNotFoundException("Holel already exist with this name : " + hotel.getName());
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
    }
}
