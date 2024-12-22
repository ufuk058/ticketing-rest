package com.rest.ticketing_rest.enums;

public enum Status {

    OPEN("Open"), IN_PROGRESS("In Progress"), COMPLETE("Complete");

    private final String value;
    Status(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
