package com.rest.ticketing_rest.exception;

public class UserAlreadyExistException  extends RuntimeException{

    public UserAlreadyExistException(String message){
        super(message);
    }
}
