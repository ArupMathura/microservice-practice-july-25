package com.example.hotel_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table
@Builder
public class Hotel {

    @Id
    private String id;
    private String name;
    private String location;
    private String about;

    public Hotel(String name, String location, String about) {
        this.name = name;
        this.location = location;
        this.about = about;
    }
}
