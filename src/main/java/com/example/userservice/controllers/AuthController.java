package com.example.userservice.controllers;

import com.example.userservice.Dtos.*;
import com.example.userservice.Exceptions.UserAlreadyExist;
import com.example.userservice.Exceptions.UserNotExist;
import com.example.userservice.Services.AuthService;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/signup")
    private ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) throws UserAlreadyExist {
        UserDto userDto=authService.signUp(request.getEmail(),request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @PostMapping("/signin")
    private ResponseEntity<UserDto> signIn(@RequestBody SignInRequestDto request) throws UserNotExist{
        return authService.signIn(request.getEmail(),request.getPassword());
    }
    @PostMapping("/validate")
    private ResponseEntity<ValidateTokenResponseDto> validateToken(@RequestBody  ValidateTokenRequestDto request){
        Optional<UserDto> userDtoOptional=authService.validateToken(request.getToken(),request.getUserId());
        if(userDtoOptional.isEmpty()){
            ValidateTokenResponseDto validateTokenResponseDto=new ValidateTokenResponseDto();
            validateTokenResponseDto.setSessionStatus(SessionStatus.INVALID);
            return new ResponseEntity<>( validateTokenResponseDto,HttpStatus.OK);
        }
        ValidateTokenResponseDto validateTokenResponseDto=new ValidateTokenResponseDto();
        validateTokenResponseDto.setSessionStatus(SessionStatus.ACTIVE);
        validateTokenResponseDto.setUserDto(userDtoOptional.get());
        return new ResponseEntity<>(validateTokenResponseDto,HttpStatus.OK);
    }
    @PostMapping("/logout")
    private ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto request){
        return authService.logOut(request.getToken(),request.getUserId());
    }
}
