package com.example.demoapi.controller;

import com.example.demoapi.model.GreetingMessage;
import com.example.demoapi.model.GreetingMessageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping(value = "/greeting")
    public ResponseEntity greeting() {
        GreetingMessage message = GreetingMessage.builder().message("Hello").build();
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity setGreetingMessage(@RequestBody @Validated GreetingMessageRequest greetingMessageRequest){

        // save greetingMessageRequest

        return ResponseEntity.ok().build();
    }
}
