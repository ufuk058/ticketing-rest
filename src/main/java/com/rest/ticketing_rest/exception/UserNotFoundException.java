package com.rest.ticketing_rest.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){

        super(message);
    }
}
