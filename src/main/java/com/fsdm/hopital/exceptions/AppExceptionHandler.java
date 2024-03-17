package com.fsdm.hopital.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotSavedException.class)
    public ResponseEntity<UserNotSavedException> handleUserNotSavedException(UserNotSavedException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.badRequest().body(e);
    }
}
