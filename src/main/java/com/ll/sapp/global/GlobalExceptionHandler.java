package com.ll.sapp.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

// 모든 컨트롤러에서 발생하는 예외를 처리하기 위한 클래스
@ControllerAdvice
public class GlobalExceptionHandler {

    // RuntimeException을 처리하기 위한 핸들러 메소드
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        // 에러 메시지 생성
        String errorMessage = ex.getMessage();

        // 에러 응답 객체 생성
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);

        // ResponseEntity를 통해 HTTP 상태 코드와 에러 응답을 반환
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 커스텀 에러 응답 클래스
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private int statusCode;
        private String message;
    }
}