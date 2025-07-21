package com.example.revisionSpringBoot.controller;

import com.example.revisionSpringBoot.dto.UserDto;
import com.example.revisionSpringBoot.entity.User;
import com.example.revisionSpringBoot.exception.ErrorDetails;
import com.example.revisionSpringBoot.exception.ResourceNotFoundException;
import com.example.revisionSpringBoot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/users")
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        log.info("in user controller : received user : -----> {}", user);
        UserDto savedUser = userService.createUser(user);
        log.info("in user controller : user id = {} name = {} {} email = {}", user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String userId) {
        UserDto getUser = userService.getUserById(userId);
        log.info("in user controller : user id --> {}, name --> {} {}, email --> {}, password -->{}", getUser.getId(), getUser.getFirstName(), getUser.getLastName(), getUser.getEmail(), getUser.getPassword());
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userList = userService.getAllUsers();
        log.info("in user controller : All Users: {}", userList);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable("id") String userId, @RequestBody UserDto userDto) {
        UserDto updateUser = userService.updateUserById(userId, userDto);
        log.info("in user controller : user id --> {}, name --> {} {}, email --> {}", updateUser.getId(), updateUser.getFirstName(), updateUser.getLastName(), updateUser.getEmail());
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String userId) {
        log.info("in user controller : received user id : -----> {}", userId);
        userService.deleteUserById(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    // update firstName and lastName by email
    // http://localhost:8080/api/users/update?email=abcd@example.com
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUserByEmail(
            @RequestParam String email,
            @RequestBody Map<String, String> userDetails) {

        String firstName = userDetails.get("firstName");
        String lastName = userDetails.get("lastName");

        Map<String, Object> response = userService.updateUserByEmail(email, firstName, lastName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // We use this @ExceptionHandler annotation to handle the specific exception and return the custom error response back to the client.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
//                exception.getLocalizedMessage(),
                HttpStatus.NOT_FOUND.value(),
                "USER_NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
