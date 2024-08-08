package telran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	
	@ExceptionHandler ( {AlreadyExistException.class})
	ResponseEntity<String> alreadyExist(AlreadyExistException e) throws Exception {
		String message = e.getMessage();		
		return errorResponse(message, HttpStatus.NOT_ACCEPTABLE);		
	}
	
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	ResponseEntity<String> notReadable(HttpMessageNotReadableException e) {
		String message = e.getMessage();		
		return errorResponse(message, HttpStatus.NOT_ACCEPTABLE);		
	}
	
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	ResponseEntity<String> notValid(MethodArgumentNotValidException e) throws Exception {
		String message = e.getMessage();
		return errorResponse(message, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler( {IllegalArgumentException.class})
	ResponseEntity<String> illegalArgument(IllegalArgumentException e) throws Exception {
		String message = e.getMessage();
		return errorResponse(message, HttpStatus.NOT_ACCEPTABLE);
		
	}
	

}