package com.banggayeon.workflow_approval_dispatcher.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(NotFoundException e){
        return Map.of(
            "code", "NOT_FOUND",
            "message", e.getMessage(),
            "timestamp", Instant.now().toString()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> validation(MethodArgumentNotValidException e){
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ":" + err.getDefaultMessage())
                .orElse("Validation error");
        return Map.of(
            "code", "VALIDATION_ERROR",
            "message", msg,
            "timestamp", Instant.now().toString()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badState(IllegalStateException e){
        return Map.of(
                "code", "BAD_STATE",
                "message", e.getMessage(),
                "timestamp", Instant.now().toString()
        );
    }
}