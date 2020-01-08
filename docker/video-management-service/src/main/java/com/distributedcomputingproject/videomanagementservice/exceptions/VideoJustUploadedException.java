package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoJustUploadedException extends RuntimeException{
    public VideoJustUploadedException(Integer id) {
        super("Video with id " + id +" just uploaded");
    }
}
