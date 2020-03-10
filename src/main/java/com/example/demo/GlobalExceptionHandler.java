package com.example.demo;

import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TransactionSystemException.class})
    protected ResponseEntity<String> handleConstraintException(){
        return ResponseEntity.badRequest().body("Validation Error");
    }

    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    protected ResponseEntity<?> handleBadCredentialException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Request Unauthenticated");
    }

    @ExceptionHandler(value = {JsonParseException.class})
    protected ResponseEntity<String> handleMalformedJwtException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Request Forbidden");
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<String> handleAccessDeniedException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Request Forbidden");
    }

}
