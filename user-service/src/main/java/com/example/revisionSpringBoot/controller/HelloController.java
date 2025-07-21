package com.example.revisionSpringBoot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello-controller")
    public String helloController() {
        return "hello love! how are you doing?";
    }
}
