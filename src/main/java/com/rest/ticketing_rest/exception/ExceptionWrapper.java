package com.rest.ticketing_rest.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExceptionWrapper {
    private LocalDateTime timeStamp;
    private Integer status;
    private String message;

    public ExceptionWrapper(String message,Integer status) {
        this.status = status;
        this.message = message;
        this.timeStamp=LocalDateTime.now();
    }
}
