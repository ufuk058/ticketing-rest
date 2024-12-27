package com.rest.ticketing_rest.exception;

import com.rest.ticketing_rest.dto.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> userNotFoundException(UserNotFoundException exception){
        exception.printStackTrace();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(exception.getMessage(),HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionWrapper> userAlreadyExistException(UserAlreadyExistException exception){
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionWrapper(exception.getMessage(),HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    public ResponseEntity<ExceptionWrapper> generalException(Throwable exception){
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionWrapper("An error occurred, Please try again later",
                                                                                                HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
