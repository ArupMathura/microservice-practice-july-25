package com.example.hotel_service.controller;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hotels")
@Tag(name = "Hotel API", description = "Operations related to hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    @Operation(summary = "Save new hotel", description = "Save new hotel")
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotel));
    }

    @GetMapping("/{hotelId}")
    @Operation(summary = "Get hotel by hotelId", description = "Returns hotel details for a given hotelId")
    public ResponseEntity<Hotel> getHotelByHotelId(@PathVariable String hotelId) {
        return ResponseEntity.status(HttpStatus.OK).body(hotelService.get(hotelId));
    }

    @GetMapping
    @Operation(summary = "Get all hotels", description = "Returns all hotels detail")
    public ResponseEntity<List<Hotel>> getAll() {
        return ResponseEntity.ok(hotelService.getAll());
    }
}
