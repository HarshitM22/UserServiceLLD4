package com.example.userservice.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequestDto {
    private String token;
    private Long userId;
}
