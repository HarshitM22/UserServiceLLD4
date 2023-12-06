package com.example.userservice.Dtos;

import com.example.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {
    private String token;
    private Long userId;
}
