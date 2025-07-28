package com.example.revisionSpringBoot.repository;

import com.example.revisionSpringBoot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.firstName = :firstName, u.lastName = :lastName where u.email = :email")
    void updateUserByEmail(String email, String firstName, String lastName);
}
