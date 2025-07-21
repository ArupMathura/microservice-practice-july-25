package com.example.revisionSpringBoot.service.impl;

import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.entity.User;
import com.example.revisionSpringBoot.exception.ResourceNotFoundException;
import com.example.revisionSpringBoot.mapper.AutoUserMapper;
import com.example.revisionSpringBoot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AutoUserMapper autoUserMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId("5");

        userDto = new UserDto();
        userDto.setId("5");
    }

    @Test
    public void testGetUserById() {

        when(userRepository.findById("5")).thenReturn(Optional.of(user));
        when(autoUserMapper.mapToUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById("5");

        assertNotNull(result);
        assertEquals(5, userDto.getId());
    }

    @Test
    public void testCreateUser() {

        when(autoUserMapper.mapToUserEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(autoUserMapper.mapToUserDto(user)).thenReturn(userDto);

        UserDto createUser = userService.createUser(userDto);

        assertNotNull(createUser);
        assertEquals(userDto.getId(), createUser.getId());

//        This checks that save() method was called exactly once.
        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void testGetAllUsers() {

        User user1 = new User();
        user1.setId("1");
        User user2 = new User();
        user1.setId("2");

        UserDto userDto1 = new UserDto();
        userDto1.setId("1");
        UserDto userDto2 = new UserDto();
        userDto2.setId("2");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(autoUserMapper.mapToUserDto(user1)).thenReturn(userDto1);
        when(autoUserMapper.mapToUserDto(user2)).thenReturn(userDto2);

        List<UserDto> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        assertEquals(1, allUsers.get(0).getId());
        assertEquals(2, allUsers.get(1).getId());

        verify(userRepository, times(1)).findAll();
        verify(autoUserMapper, times(2)).mapToUserDto(any(User.class));
    }

    @Test
    public void testUpdateUserById() {
        when(userRepository.findById("5")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(autoUserMapper.mapToUserDto(user)).thenReturn(userDto);

        UserDto updateUser = userService.updateUserById("5", userDto);

        assertNotNull(updateUser);
        assertEquals(userDto.getId(), updateUser.getId());
        assertEquals(userDto.getFirstName(), updateUser.getFirstName());
        assertEquals(userDto.getEmail(), updateUser.getEmail());

        verify(userRepository, times(1)).findById(userDto.getId());
        verify(userRepository, times(1)).save(user);
        verify(autoUserMapper, times(1)).mapToUserDto(user);
    }

    /*@Test
    public void testDeleteUserById() {
        doNothing().when(userRepository).deleteById(5);
        userRepository.deleteById(5);
        verify(userRepository, times(1)).deleteById(5);
    }*/

    @Test
    public void testDeleteUserById() {
        String userId = "5";

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void getUpdateUserByEmail() {
        String email = "abcd@gmail.com";
        String firstName = "abcd";
        String lastName = "efgh";

        // Suppose user is already available in DB
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, Object> response = userService.updateUserByEmail(email, firstName, lastName);

        assertNotNull(email);
        assertEquals(email, response.get("email"));
        assertEquals(user.getId(), response.get("id"));
        log.info("------------------user.getId() -------------{}", user.getId());
        log.info("------------------response.get(\"id\") -------------{}  ", response.get("id"));
        assertEquals("firstname - \"" + firstName + "\" and lastname - \"" + lastName + "\" updated", response.get("msg"));

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).updateUserByEmail(email, firstName, lastName);
    }

    @Test
    void testUpdateUserByEmail_UserNotFound() {
        String email = null;
        String firstName = null;
        String lastName = null;

        // Mock behavior: findByEmail returns null (user not found)
        when(userRepository.findByEmail(email)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUserByEmail(email, firstName, lastName);
        });

        verify(userRepository, times(1)).findByEmail(email);
//        assertEquals("User not found with email : unknown@gmail.com", exception.getMessage());
    }

}