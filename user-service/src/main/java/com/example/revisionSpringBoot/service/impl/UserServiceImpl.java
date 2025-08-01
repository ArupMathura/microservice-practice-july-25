package com.example.revisionSpringBoot.service.impl;

import com.example.revisionSpringBoot.dto.RatingDto;
import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.entity.Hotel;
import com.example.revisionSpringBoot.entity.Rating;
import com.example.revisionSpringBoot.entity.User;
import com.example.revisionSpringBoot.exception.ResourceNotFoundException;
import com.example.revisionSpringBoot.feignClientServices.HotelServiceFeignClient;
import com.example.revisionSpringBoot.mapper.AutoUserMapper;
//import com.example.revisionSpringBoot.mapper.UserMapper;
import com.example.revisionSpringBoot.repository.UserRepository;
import com.example.revisionSpringBoot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private AutoUserMapper autoUserMapper;

    private RestTemplate restTemplate;

    private HotelServiceFeignClient hotelServiceFeignClient;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        System.out.println("in user service implementation : User ID before saving: " + userDto.getId());
//        User user = UserMapper.mapToUserEntity(userDto);

        // Encrypt the password
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

        User user = autoUserMapper.mapToUserEntity(userDto);
        log.info("before saving : ----------> {};  {};  {}", user.getId(),user.getEmail(), user.getPassword());

        User saveUser = userRepository.save(user);
        log.info("after saving : ----------> {};  {};  {}", user.getId(),user.getEmail(), user.getPassword());

//        UserDto saveUserDto = UserMapper.mapToUserDto(saveUser);
        UserDto saveUserDto = autoUserMapper.mapToUserDto(saveUser);
        return saveUserDto;
    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("in user service implementation : Fetching user id : -----> {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
//        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
//        return UserMapper.mapToUserDto(user);

        // // Step 1: Fetch all ratings by user from RATING-SERVICE
        // localhost:8083/api/ratings/users/e07406c8-b149-46d9-a33f-a1257f9cf68e

        ResponseEntity<List<Rating>> ratingResponse =
                restTemplate.exchange(
                        "http://RATING-SERVICE/api/ratings/users/" + userId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Rating>>() {}
                );

        List<Rating> ratingsOfUser = ratingResponse.getBody();
        log.info("Ratings of user: {}", ratingsOfUser);

        // Step 2: For each rating, call HOTEL-SERVICE using hotelId
        // http://localhost:8082/api/hotels/eb4189df-45c7-4025-8227-fc978cc5ea18
        List<Rating> enrichedRatings = ratingsOfUser.stream().map(rating -> {

            Hotel hotel = hotelServiceFeignClient.getHotelById(rating.getHotelId());

            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(enrichedRatings);
        return autoUserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
//        return users.stream()
//                .map(UserMapper::mapToUserDto)
//                .collect(Collectors.toList());

        return users.stream()
                .map(autoUserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUserById(String userId, UserDto user) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        log.info("in user service implementation : user id --> {}, name --> {} {}, email --> {}", existingUser.getId(), existingUser.getFirstName(), existingUser.getLastName(), existingUser.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        User updateUser = userRepository.save(existingUser);
        log.info("in user service implementation : user id --> {}, name --> {} {}, email --> {}", updateUser.getId(), updateUser.getFirstName(), updateUser.getLastName(), updateUser.getEmail());
//        return UserMapper.mapToUserDto(updateUser);
        return autoUserMapper.mapToUserDto(updateUser);
    }

    public void deleteUserById(String userId) {
//        UUID uuid = UUID.fromString(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public Map<String, Object> updateUserByEmail(String email, String firstName, String lastName) {
        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }

        // update user's firstName and lastName
        userRepository.updateUserByEmail(email, firstName, lastName);

        // prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("id", existingUser.getId());
        response.put("msg", "firstname - \"" + firstName + "\" and lastname - \"" + lastName + "\" updated");

        return response;
    }


}
