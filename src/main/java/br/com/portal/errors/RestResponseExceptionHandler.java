package br.com.portal.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.portal.errors.exceptions.EmailAlreadyExistsException;
import br.com.portal.model.ErrorMessage;
import br.com.portal.util.MessageUtil;

/**
 * Handle para exceptions
 * @author douglas.takara
 */
@ControllerAdvice
@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageUtil messageUtil;
	
	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<Object> handleAccessDeniedException(Exception e, WebRequest request){
		return new ResponseEntity<Object>(new ErrorMessage(messageUtil.getMessage(e.getMessage())),
				new HttpHeaders(), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler({EmailAlreadyExistsException.class})
	public ResponseEntity<Object> handleEmailAlreadyExistsException(Exception e){
		return new ResponseEntity<Object>(new ErrorMessage(messageUtil.getMessage("user.mail.already_exist")), 
				new HttpHeaders(),
				HttpStatus.CONFLICT);
	}
}