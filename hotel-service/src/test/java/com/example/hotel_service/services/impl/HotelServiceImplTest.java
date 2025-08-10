package com.example.hotel_service.services.impl;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class HotelServiceImplTest {

    @Autowired
    private HotelServiceImpl hotelService;

    @MockitoBean
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        // clean up database before each test
        hotelRepository.deleteAll();
    }

    @Test
    void create() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID().toString());
        hotel.setName("Test Hotel");
        hotel.setAbout("Test About");
        hotel.setLocation("Test Location");

        // Mock the hotelRepository.save method
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        // Act
        Hotel savedHotel = hotelService.create(hotel);

        // Assert
        assertThat(savedHotel.getId()).isNotNull();
        assertThat(savedHotel.getName()).isEqualTo("Test Hotel");
        assertThat(savedHotel.getAbout()).isEqualTo("Test About");
        assertThat(savedHotel.getLocation()).isEqualTo("Test Location");

    }

//    @Test
//    void create_WithNullHotel_ShouldThrowException() {
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> hotelService.create(null));
//    }

    @Test
    void getAll() {
    }

    @Test
    void get() {
    }
}