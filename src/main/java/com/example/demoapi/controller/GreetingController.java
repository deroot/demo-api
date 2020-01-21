package com.example.demoapi.controller;

import com.example.demoapi.model.GreetingMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public ResponseEntity greeting() {
        GreetingMessage message = GreetingMessage.builder().message("Hello").build();
        return ResponseEntity.ok(message);
    }
}
