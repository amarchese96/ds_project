package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoAlreadyExistsException extends RuntimeException{

    public VideoAlreadyExistsException(String name) {
        super("Video with name " + name +" already exists");
    }
}
