package com.pseong.spring3template.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        // log.error(BaseResponseStatus.REQUEST_ERROR.getMessage());
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new ResponseEntity<>(new BaseResponse<Map<String, String>>(BaseResponseStatus.REQUEST_ERROR, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseException> baseException(BaseException e) {
        // log.error(e.getStatus().getMessage());
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> bindException(BindException exception) {
        // log.error(BaseResponseStatus.REQUEST_ERROR.getMessage());
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new ResponseEntity<>(new BaseResponse<Map<String, String>>(BaseResponseStatus.REQUEST_ERROR, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<String> exception(Exception exception) {
        // log.error(exception.getMessage());
        return new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}