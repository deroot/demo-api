package com.example.demoapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GreetingMessageRequest {
    @NotBlank
    private String message;
    @NotBlank
    private String createBy;
}
