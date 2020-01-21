package com.example.demoapi.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private HttpStatus status;
    private String message;
    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
