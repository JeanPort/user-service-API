package com.jean.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalHadlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultMessage> handleNotFoundException(NotFoundException e) {
        var error = new DefaultMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<DefaultMessage> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        var error = new DefaultMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
