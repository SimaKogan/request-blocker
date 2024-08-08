package telran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private ResponseEntity<String> errorResponse(String body, HttpStatus status) {
		log.error(body);
		return new ResponseEntity<>(body, status);
	}

	@ExceptionHandler({ NotFoundException.class })
	ResponseEntity<String> notFound(NotFoundException e) {
		String message = e.getMessage();
		return errorResponse(message, HttpStatus.NOT_FOUND);
	}

}