package com.example.revisionSpringBoot.mapper;

import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring") // Enables Spring to inject this mapper
public interface AutoUserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password")
    })
    UserDto mapToUserDto(User user);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password")
    })
    User mapToUserEntity(UserDto userDto);
}
