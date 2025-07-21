package com.example.revisionSpringBoot.service.impl;

import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.entity.User;
import com.example.revisionSpringBoot.exception.ResourceNotFoundException;
import com.example.revisionSpringBoot.mapper.AutoUserMapper;
//import com.example.revisionSpringBoot.mapper.UserMapper;
import com.example.revisionSpringBoot.repository.UserRepository;
import com.example.revisionSpringBoot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private AutoUserMapper autoUserMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        System.out.println("in user service implementation : User ID before saving: " + userDto.getId());
//        User user = UserMapper.mapToUserEntity(userDto);
        User user = autoUserMapper.mapToUserEntity(userDto);

        User saveUser = userRepository.save(user);

//        UserDto saveUserDto = UserMapper.mapToUserDto(saveUser);
        UserDto saveUserDto = autoUserMapper.mapToUserDto(saveUser);
        return saveUserDto;
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uuid = UUID.fromString(userId);
        log.info("in user service implementation : received user id : -----> {}", userId);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", uuid));
//        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
//        return UserMapper.mapToUserDto(user);
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
        UUID uuid = UUID.fromString(userId);
        User existingUser = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
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
        UUID uuid = UUID.fromString(userId);
        userRepository.deleteById(uuid);
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
