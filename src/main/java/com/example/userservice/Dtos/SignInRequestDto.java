package com.example.userservice.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDto {
    private String Email;
    private String password;

}
