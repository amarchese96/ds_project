package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoNotFoundException extends RuntimeException {

    public VideoNotFoundException(Integer id) {
        super("Video with id " + id +" not found");
    }
}
