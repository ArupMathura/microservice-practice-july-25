package com.example.hotel_service.controller;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.repositories.HotelRepository;
import com.example.hotel_service.services.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(properties = {
        "spring.config.additional-location=classpath:yaml/"
})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class HotelControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    HotelService hotelService;

    @BeforeEach
    void setup() {
        hotelRepository.deleteAll(); // clean DB before each test
    }

    @Test
    void testCreateNewHotel () throws Exception {

        String newHotelJson = """
                {
                    "name": "Taj",
                    "location": "mumbai",
                    "about": "Luxury stay"
                }
                """;

        mockMvc.perform(post("/api/hotels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(newHotelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(emptyString())))
                .andExpect(jsonPath("$.name").value("Taj"));
    }

    @Test
    void testCreateHotel_DuplicateName_ShouldMatchJsonFile() throws Exception {

        // Arrange
        Hotel existingHotel = new Hotel();
        existingHotel.setId(UUID.randomUUID().toString());
        existingHotel.setName("Abcd");
        existingHotel.setLocation("Delhi");
        existingHotel.setAbout("Luxury");
        hotelRepository.save(existingHotel);

        String newHotelJson = """
            {
              "name": "Abcd",
              "location": "Mumbai",
              "about": "Business"
            }
            """;

        // Read expected JSON
        Path filePath = Paths.get("src/test/resources/expected-responses/hotel-duplicate.json");
        String expectedJson = Files.readString(filePath);

        // Act + Assert
        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newHotelJson))
                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Hotel already exist with this name : Taj"))
                .andExpect(content().json(expectedJson));
    }


    @Test
    void testCreateHotel_DuplicateName_ShouldMatchJsonFile1() throws Exception {

        Hotel existingHotel = new Hotel();
        existingHotel.setId(UUID.randomUUID().toString());
        existingHotel.setName("Abcd");
        existingHotel.setLocation("Delhi");
        existingHotel.setAbout("Luxury");
        hotelRepository.save(existingHotel);

        Hotel newHotel = new Hotel();
        newHotel.setName("Abcd");
        newHotel.setLocation("Mumbai");
        newHotel.setAbout("Business");

        String expectedJson = """
            {
              "success": false,
              "message": "Hotel already exist with this name : Abcd",
              "status": "NOT_FOUND"
            }
            """;

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHotel)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Hotel already exist with this name : Abcd"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }

    @Test
    void testGetHotelById_ShouldReturnHotel() throws Exception {
        // Arrange: Save a hotel in the DB
        Hotel hotel = new Hotel();
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        hotel.setName("Oberoi");
        hotel.setLocation("Kolkata");
        hotel.setAbout("Business hotel");
        hotelRepository.save(hotel);

        // Act & Assert: Retrieve by ID
        mockMvc.perform(get("/api/hotels/" + hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").value("Oberoi"))
                .andExpect(jsonPath("$.location").value("Kolkata"))
                .andExpect(jsonPath("$.about").value("Business hotel"));
    }

    @Test
    void testGetHotelById_NotFound_ShouldReturnError() throws Exception {
        // Act & Assert: Try with a non-existing ID
        String unknownId = UUID.randomUUID().toString();

        // Read expected JSON
        Path filePath = Paths.get("src/test/resources/expected-responses/resource-not-found.json");
        String expectedJson = Files.readString(filePath);

        mockMvc.perform(get("/api/hotels/" + unknownId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("resource not found"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetAllHotels_ShouldReturnHotelList() throws Exception {
        // Arrange: Insert two hotels
        Hotel hotel1 = new Hotel();
        hotel1.setId(UUID.randomUUID().toString());
        hotel1.setName("Taj");
        hotel1.setLocation("Mumbai");
        hotel1.setAbout("Luxury");

        Hotel hotel2 = new Hotel();
        hotel2.setId(UUID.randomUUID().toString());
        hotel2.setName("ITC");
        hotel2.setLocation("Delhi");
        hotel2.setAbout("Business");

        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        // Act & Assert: Get all
        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Taj"))
                .andExpect(jsonPath("$[1].name").value("ITC"));
    }

    @Test
    void testGetAllHotels_Empty_ShouldReturnEmptyList() throws Exception {
        // Arrange: Ensure DB is empty
        hotelRepository.deleteAll();

        // Act & Assert: Get all
        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


}