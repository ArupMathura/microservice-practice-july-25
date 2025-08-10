package com.example.hotel_service.services.impl;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // This initializes mocks
class HotelServiceImplTests2 {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void create() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID().toString());
        hotel.setName("Test Hotel");
        hotel.setAbout("Test About");
        hotel.setLocation("Test Location");

        // Mock the save method
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        // Act
        Hotel savedHotel = hotelService.create(hotel);

        // Assert
        assertThat(savedHotel.getId()).isNotNull();
        assertThat(savedHotel.getName()).isEqualTo("Test Hotel");
        assertThat(savedHotel.getAbout()).isEqualTo("Test About");
        assertThat(savedHotel.getLocation()).isEqualTo("Test Location");
    }

}
