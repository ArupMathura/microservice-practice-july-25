package com.example.revisionSpringBoot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore             // This hides the password field in the response
    @Column(nullable = false)
    private String password;

    /*@Transinent : It tells Hibernate not to store this field in the database,
    but it will be included in the response if it has a getter and is not marked with @JsonIgnore.*/

    @Transient
    private List<Rating> ratings = new ArrayList<>();

    @SuppressWarnings("unused")
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // You used @JsonIgnore correctly on the password field, but it's still showing in the response.
    // still showing because of Lombok issue — add json ignore in getter
    @JsonIgnore
    public List<Rating> getRatings() {
        return ratings;
    }

}
