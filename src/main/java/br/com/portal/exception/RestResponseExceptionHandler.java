package br.com.portal.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handle para exceptions
 * @author douglas.takara
 */
@ControllerAdvice
@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<Object> handleAccessDeniedException(Exception e, WebRequest request){
		return new ResponseEntity<Object>("message", new HttpHeaders(), HttpStatus.FORBIDDEN);
	}
}