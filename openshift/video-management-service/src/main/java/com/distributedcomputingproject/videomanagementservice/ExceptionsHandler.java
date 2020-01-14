package com.distributedcomputingproject.videomanagementservice;

import com.distributedcomputingproject.videomanagementservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ExceptionsHandler {

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String userAlreadyExistsHandler(UserAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(VideoAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String videoAlreadyExistsHandler(VideoAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(VideoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String videoNotFoundHandler(VideoNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(VideoJustAvailableException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String videoJustAvailable(VideoJustAvailableException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(VideoUploadingNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String videoUploadingNotAllowed(VideoUploadingNotAllowedException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(VideoUploadingErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String videoUploadingError(VideoUploadingErrorException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(VideoNotWaitingUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String videoNotWaitingUpload(VideoNotWaitingUploadException exception) {
        return exception.getMessage();
    }

}
