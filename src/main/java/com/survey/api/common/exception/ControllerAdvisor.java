package com.survey.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.survey.api.common.dto.Response;


@ControllerAdvice
public class ControllerAdvisor {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> resourceNotFoundException(BadRequestException ex, WebRequest request) {
		ex.printStackTrace();
//		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		Response errorDetails= new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),  ex.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessNotAllowedException.class)
	public ResponseEntity<?> accessNotAllowedException(AccessNotAllowedException ex, WebRequest request) {
		ex.printStackTrace();

//		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		Response errorDetails= new Response(HttpStatus.FORBIDDEN.value(),  ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
		ex.printStackTrace();
//		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		Response errorDetails= new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),  ex.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(FileStorageException.class)
	public ResponseEntity<Object> FileStorageException(FileStorageException ex, WebRequest request) {

//		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		Response errorDetails= new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),  ex.getMessage());

		//Response res = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<Object> ObjectNotFoundException(ObjectNotFoundException ex, WebRequest request) {

//		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		Response errorDetails= new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),  ex.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}


//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//		Map<String, Object> body = new LinkedHashMap<>();
//		body.put("timestamp", LocalDate.now());
//		body.put("status", status.value());
//
//		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
//				.collect(Collectors.toList());
//
//		body.put("errors", errors);
//
//		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//	}

}
