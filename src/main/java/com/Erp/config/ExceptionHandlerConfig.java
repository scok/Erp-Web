package com.Erp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//예외를 감지하고 롤백을 수행하는 클래스 입니다.
@ControllerAdvice
public class ExceptionHandlerConfig{
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        // 롤백 수행
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        // 예외 처리 로직
        // ...
        String message = ex.getMessage();

        // ResponseEntity 반환
        // ...
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
