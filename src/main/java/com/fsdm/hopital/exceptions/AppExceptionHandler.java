package com.fsdm.hopital.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    public record ErrorResponseDTO(ProcessingException type , String message){};
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDTO> handleAppException(AppException e) {
        e.printStackTrace();
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDTO(e.getErrorType() , e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDTO(ProcessingException.INTERNAL_ERROR , e.getMessage()));
    }
}
