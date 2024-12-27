package com.rest.ticketing_rest.exception;

import com.rest.ticketing_rest.dto.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionWrapper> handleValidationError(MethodArgumentNotValidException exception, HttpServletRequest request){
        exception.printStackTrace();
        ExceptionWrapper exceptionWrapper= new ExceptionWrapper("Invalid Input(s)", HttpStatus.BAD_REQUEST.value());
        exceptionWrapper.setPath(request.getRequestURI());
        List<ValidationException> validationExceptions= new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String errorField= ((FieldError)error).getField();
            Object rejectedValue= ((FieldError) error).getRejectedValue();
            String reason =error.getDefaultMessage();
            ValidationException validationException= new ValidationException(errorField,rejectedValue,reason);
            validationExceptions.add(validationException);
        }

        exceptionWrapper.setValidationExceptions(validationExceptions);
        exceptionWrapper.setErrorCount(validationExceptions.size());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionWrapper);
    }
}
