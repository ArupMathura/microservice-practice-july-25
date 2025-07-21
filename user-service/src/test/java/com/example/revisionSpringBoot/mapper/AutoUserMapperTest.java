package com.example.revisionSpringBoot.mapper;

import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class AutoUserMapperTest {

    // Create mapper instance manually
    private AutoUserMapper autoUserMapper = Mappers.getMapper(AutoUserMapper.class);

    @Test
    void testMapToUserDto() {
        User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");

        UserDto userDto = autoUserMapper.mapToUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPassword(), userDto.getPassword());
    }

    @Test
    void testMapToUserEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john@example.com");
        userDto.setPassword("password");

        User user = autoUserMapper.mapToUserEntity(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    void testMapToUserDto_NullInput() {
        UserDto userDto = autoUserMapper.mapToUserDto(null);
        assertNull(userDto, "Expected null when input user is null");
    }

    @Test
    void testMapToUserEntity_NullInput() {
        User user = autoUserMapper.mapToUserEntity(null);
        assertNull(user, "Expected null when input userDto is null");
    }
}
