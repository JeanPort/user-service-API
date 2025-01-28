package com.jean.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHadlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultMessage> handleNotFoundException(NotFoundException e) {
        var error = new DefaultMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
