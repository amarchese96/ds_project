package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoJustAvailableException extends RuntimeException{
    public VideoJustAvailableException(Integer id) {
        super("Video with id " + id +" just uploaded");
    }
}
