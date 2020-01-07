package com.distributedcomputingproject.videomanagementservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("User with email " + email +" already exists");
    }
}
