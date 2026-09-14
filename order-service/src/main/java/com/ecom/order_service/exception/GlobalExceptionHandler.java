package com.ecom.order_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(OrderNotFoundException.class)
	public  ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex){
		
		ErrorResponse response= new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		
	}

}
