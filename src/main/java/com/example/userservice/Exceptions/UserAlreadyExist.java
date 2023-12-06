package com.example.userservice.Exceptions;

import com.example.userservice.models.User;

public class UserAlreadyExist extends Exception{
    public UserAlreadyExist(String message){
        super(message);
    }
}
