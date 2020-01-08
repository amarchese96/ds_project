package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoUploadingNotAllowedException extends RuntimeException {

    public VideoUploadingNotAllowedException(Integer id) {
        super("Uploading video with id " + id +" not allowed");
    }
}
