package tw.niq.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler
	public ResponseEntity<List<Map<String, String>>> handleJpaViolations(TransactionSystemException e) {
		
		ResponseEntity.BodyBuilder responseEntityBodyBuilder = ResponseEntity.badRequest();
		
		if (e.getCause().getCause() instanceof ConstraintViolationException) {

			ConstraintViolationException cve = (ConstraintViolationException) e.getCause().getCause();
			
			List<Map<String, String>> errors = cve.getConstraintViolations()
					.stream()
					.map(constraintViolation -> {
						Map<String, String> error = new HashMap<>();
						error.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
						return error;
					})
					.collect(Collectors.toList());

			return responseEntityBodyBuilder.body(errors);
		}

		return responseEntityBodyBuilder.build();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException e) {
		
		List<Map<String, String>> errors = e.getFieldErrors()
				.stream()
				.map(fieldError -> {
					Map<String, String> error = new HashMap<>();
					error.put(fieldError.getField(), fieldError.getDefaultMessage());
					return error;
				})
				.collect(Collectors.toList());
		
		return ResponseEntity.badRequest().body(errors);
	}
	
}
