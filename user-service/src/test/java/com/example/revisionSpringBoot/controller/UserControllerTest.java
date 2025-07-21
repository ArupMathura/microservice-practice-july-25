package com.example.revisionSpringBoot.controller;

import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)  // Only load the UserController (not whole application)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // For performing HTTP requests in test

    @MockBean
    private UserService userService; // Mock the service that controller depends on

    @Autowired
    private ObjectMapper objectMapper; // to convert Object to JSON

    private UserDto userDto;

    private String id = "5";
    private String firstName = "mnop";
    private String lastName = "xyz";
    private String email = "mnop@gmail.com";

    @BeforeEach
    void setUp() {
        // Step 1: Prepare dummy UserDto
        userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
    }

    @Test
    void testCreateUser() throws Exception {
        // Step 2: Mock the service
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        // Convert user to JSON string
        String jsonString = objectMapper.writeValueAsString(userDto);

        //Step 3: Perform POST request
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserIdIsValid() throws Exception {
        // Arrange
//        UserDto userDto = new UserDto();
//        userDto.setId(5);
//        userDto.setFirstName("John");
//        userDto.setLastName("Doe");
//        userDto.setEmail("john.doe@example.com");
//        userDto.setPassword("password123");

        when(userService.getUserById(id)).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/api/users/5"))  // simulate GET /api/users/5
                .andExpect(status().isOk()) // expect HTTP 200 OK
                .andExpect(jsonPath("$.id").value(id)) // expect JSON field id = 5
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDto> userDtoList = Arrays.asList(
                new UserDto("1", "aaaa", "bbbb", "aaa@gmail.com", "1234"),
                new UserDto("2", "cccc", "dddd", "ccc@gmail.com", "2345"),
                new UserDto("3", "eeee", "ffff", "eee@gmail.com", "3456")
        );

        when(userService.getAllUsers()).thenReturn(userDtoList);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("aaaa"))
                .andExpect(jsonPath("$[0].lastName").value("bbbb"))
                .andExpect(jsonPath("$[0].email").value("aaa@gmail.com"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("cccc"))
                .andExpect(jsonPath("$[1].lastName").value("dddd"))
                .andExpect(jsonPath("$[1].email").value("ccc@gmail.com"))
        ;
    }

    @Test
    void testUpdateUserById() throws Exception {
//        int userId = 5;

        when(userService.updateUserById(eq(id), any(UserDto.class))).thenReturn(userDto);

        // Convert user to JSON string
        String jsonString = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                put("/api/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(firstName));
    }

    @Test
    void testDeleteUserById() throws Exception {
        doNothing().when(userService).deleteUserById(id);
        mockMvc.perform(delete("/api/users/5"))  // simulate GET /api/users/5
                .andExpect(status().isOk());

        // Optional but Good: verify that service method was called once
        verify(userService, times(1)).deleteUserById(id);
    }

    @Test
    void testUpdateUserByEmail() throws Exception {
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";

        // Create request body - userDetails
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);

        // Create request body
//        Map<String, String> userDetails = new HashMap<>();
//        userDetails.put("firstName", firstName);
//        userDetails.put("lastName", lastName);

        // Create expected response from service
        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("id", 1);  // suppose id = 1
        response.put("msg", "firstname - \"" + firstName + "\" and lastname - \"" + lastName + "\" updated");

        String requestJson = new ObjectMapper().writeValueAsString(requestBody);

        // Mock the service call
        when(userService.updateUserByEmail(eq(email), eq(firstName), eq(lastName))).thenReturn(response);

        mockMvc.perform(put("/api/users/update") // URL
                        .param("email", email) // email is passed as RequestParam
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)) // userDetails as body
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.msg").value("firstname - \"" + firstName + "\" and lastname - \"" + lastName + "\" updated"));
    }
}
