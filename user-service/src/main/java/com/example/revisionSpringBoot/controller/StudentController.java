package com.example.revisionSpringBoot.controller;

import com.example.revisionSpringBoot.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class StudentController {

    @GetMapping("students/get-student")
    public ResponseEntity<Student> getStudent() {
        Student student = new Student(101, "Arup", "Mathura");
//        return new ResponseEntity<>(student, HttpStatus.OK);
        return ResponseEntity.ok(student);
//        return ResponseEntity.ok()
//                .header("custom-header", "arup-mathura")
//                .body(student);
    }

    @GetMapping("students/get-students")
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(102, "aaa", "aaaaa"));
        students.add(new Student(103, "bbb", "bbbbb"));
        students.add(new Student(104, "ccc", "ccccc"));
        students.add(new Student(105, "ddd", "ddddd"));
        return ResponseEntity.ok(students);
    }

    // {id} --> URI template variable
    @GetMapping("students/{id}/{first-name}/{last-name}")
    public ResponseEntity<Student> studentPathVariable(@PathVariable("id") int studentId,
                                       @PathVariable("first-name") String firstName,
                                       @PathVariable("last-name") String lastName) {
        Student student = new Student(studentId, firstName, lastName);
        return ResponseEntity.ok(student);
    }

    // localhost:8080/students/query?id=111&firstName=arup&lastName=mathura
    @GetMapping("/students/query")
    public ResponseEntity<Student> studentQueryParameter(@RequestParam int id,
                                         @RequestParam String firstName,
                                         @RequestParam String lastName) {
        Student student = new Student(id, firstName, lastName);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/students/create")
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        log.info("Student ID: {}", student.getId());
        log.info("First Name: {}", student.getFirstName());
        log.info("Last Name: {}", student.getLastName());
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("students/{id}/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable("id") int studentId) {
        log.info("Updating student with ID: {}", studentId);
        log.info("First Name: {}", student.getFirstName());
        log.info("Last Name: {}", student.getLastName());

//        *************************************************************
        /*
        1. my req body :
        {
            "firstName": "arup",
            "lastName": "das"
        }
        does not contain an id.
        2. Since @RequestBody only maps the fields that exist in the request body, id remains its default value (which is 0 for an int).
        3. You need to manually set the id from the path variable (studentId) before returning the response.

         */

//        *********************************************************
        student.setId(studentId); // ✅ Set the ID from the path variable

        return ResponseEntity.ok(student);
    }

    @DeleteMapping("students/{id}/delete")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") int studentId) {
        log.warn("deleting student with id : {}", studentId);
        return ResponseEntity.ok("student info deleted successfully");
    }
}
