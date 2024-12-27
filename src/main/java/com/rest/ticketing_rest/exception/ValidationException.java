package com.rest.ticketing_rest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationException {

    private String errorField;
    private Object rejectedValue;
    private String reason;
}
