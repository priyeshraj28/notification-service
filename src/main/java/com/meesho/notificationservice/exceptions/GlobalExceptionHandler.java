package com.meesho.notificationservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception,WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDescription(webRequest.getDescription(false));
        errorResponse.setMessage(exception.getLocalizedMessage());
        if(exception instanceof MethodArgumentNotValidException) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage());
                sb.append(";");
            }
            for (ObjectError objectError : ((MethodArgumentNotValidException) exception).getBindingResult().getGlobalErrors()) {
                sb.append(objectError.getDefaultMessage());
                sb.append(";");
            }
            errorResponse.setMessage(sb.toString());
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(exception instanceof NoSuchElementException) {
            errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        else if(exception instanceof ConstraintViolationException
                || exception instanceof MethodArgumentTypeMismatchException
                || exception instanceof IllegalArgumentException) {
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else
        {
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
