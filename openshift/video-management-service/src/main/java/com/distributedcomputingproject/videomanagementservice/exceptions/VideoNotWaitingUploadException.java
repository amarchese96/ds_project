package com.distributedcomputingproject.videomanagementservice.exceptions;

public class VideoNotWaitingUploadException extends RuntimeException {

    public VideoNotWaitingUploadException(Integer id) {
        super("Video with id " + id + " not in WaitingUpload state");
    }
}
