package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoUploadingErrorException extends RuntimeException {

    public VideoUploadingErrorException(Integer id) {
        super("Error in uploading video with id " + id );
    }
}
