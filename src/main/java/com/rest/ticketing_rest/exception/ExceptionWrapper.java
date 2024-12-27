package com.rest.ticketing_rest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionWrapper {
    private LocalDateTime timeStamp;
    private Integer status;
    private String message;

    private String path;

    private Integer errorCount;
    private List<ValidationException> validationExceptions;

    public ExceptionWrapper(String message,Integer status) {
        this.status = status;
        this.message = message;
        this.timeStamp=LocalDateTime.now();
    }
}
