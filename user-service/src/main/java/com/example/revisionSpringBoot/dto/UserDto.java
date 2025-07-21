package com.example.revisionSpringBoot.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
