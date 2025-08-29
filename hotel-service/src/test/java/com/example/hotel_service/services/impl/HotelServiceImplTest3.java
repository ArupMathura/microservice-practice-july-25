package com.example.hotel_service.services.impl;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.exceptions.ResourceNotFoundException;
import com.example.hotel_service.repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceImplTest3 {

    private HotelRepository hotelRepository;
    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        hotelRepository = Mockito.mock(HotelRepository.class);
        hotelService = new HotelServiceImpl(hotelRepository);
    }

    // ------------------ create() ------------------

    @Test
    void create_ShouldSaveHotel_WhenHotelDoesNotExist() {
        // given
        Hotel hotel = Hotel.builder()
                .id("h1")
                .name("Sunrise Inn")
                .location("Miami")
                .about("Beachside hotel")
                .build();

        when(hotelRepository.findByName("Sunrise Inn")).thenReturn(Optional.empty());
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        // when
        Hotel savedHotel = hotelService.create(hotel);

        // then
        assertNotNull(savedHotel);
        assertEquals("Sunrise Inn", savedHotel.getName());
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void create_ShouldThrowException_WhenHotelAlreadyExists() {
        // given
        Hotel existingHotel = Hotel.builder()
                .id("h1")
                .name("Grand Plaza")
                .location("NYC")
                .about("Luxury stay")
                .build();

        when(hotelRepository.findByName("Grand Plaza")).thenReturn(Optional.of(existingHotel));

        Hotel newHotel = Hotel.builder()
                .id("h2")
                .name("Grand Plaza") // duplicate name
                .location("LA")
                .about("Different hotel")
                .build();

        // when + then
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> hotelService.create(newHotel));

        assertEquals("Holel already exist with this name : Grand Plaza", ex.getMessage());
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    // ------------------ getAll() ------------------

    @Test
    void getAll_ShouldReturnHotels_WhenHotelsExist() {
        // given
        Hotel hotel = Hotel.builder()
                .id("h1")
                .name("Green Valley")
                .location("Denver")
                .about("Mountain view")
                .build();

        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        // when
        List<Hotel> hotels = hotelService.getAll();

        // then
        assertEquals(1, hotels.size());
        assertEquals("Green Valley", hotels.get(0).getName());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoHotelsExist() {
        // given
        when(hotelRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Hotel> hotels = hotelService.getAll();

        // then
        assertTrue(hotels.isEmpty());
        verify(hotelRepository, times(1)).findAll();
    }

    // ------------------ get(id) ------------------

    @Test
    void get_ShouldReturnHotel_WhenHotelExists() {
        // given
        String hotelId = "h1";
        Hotel hotel = Hotel.builder()
                .id(hotelId)
                .name("City Lodge")
                .location("Chicago")
                .about("Business hotel")
                .build();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // when
        Hotel foundHotel = hotelService.get(hotelId);

        // then
        assertNotNull(foundHotel);
        assertEquals("City Lodge", foundHotel.getName());
        verify(hotelRepository, times(1)).findById(hotelId);
    }

    @Test
    void get_ShouldThrowException_WhenHotelDoesNotExist() {
        // given
        String hotelId = "not_exist";
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // when + then
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> hotelService.get(hotelId));

        assertEquals("resource not found", ex.getMessage());
        verify(hotelRepository, times(1)).findById(hotelId);
    }
}
