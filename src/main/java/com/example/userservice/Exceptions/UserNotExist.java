package com.example.userservice.Exceptions;

public class UserNotExist extends Exception{
    public UserNotExist(String message){
        super(message);
    }
}
