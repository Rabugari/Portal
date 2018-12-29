package br.com.portal.errors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.portal.errors.exceptions.AuthenticationException;
import br.com.portal.errors.exceptions.EmailAlreadyExistsException;
import br.com.portal.model.ErrorMessage;
import br.com.portal.util.MessageUtil;
import br.com.portal.util.MessageUtil.MessageConstants;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * Handle para exceptions para endpoints REST
 * 
 * @author douglas.takara
 */
@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageUtil messageUtil;

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception e, WebRequest request) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorMessage(messageUtil.getMessage(e.getMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		StringBuilder methodsErrorsSb = new StringBuilder();
		methodsErrorsSb.append(messageUtil.getMessage(MessageConstants.USER_INSERT_ERROR));
		allErrors.forEach(error -> {
			methodsErrorsSb.append(System.getProperty("line.separator"));
			methodsErrorsSb.append(messageUtil.getMessage(error.getDefaultMessage()));
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(methodsErrorsSb.toString()));
	}

	@ExceptionHandler({ UsernameNotFoundException.class })
	public ResponseEntity<Object> handlerUserNotFoundException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorMessage(messageUtil.getMessage(e.getMessage())));
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<Object> handlerAuthenticationException(Exception e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorMessage(messageUtil.getMessage(e.getMessage())));
	}

	@ExceptionHandler({ EmailAlreadyExistsException.class })
	public ResponseEntity<Object> handleEmailAlreadyExistsException(Exception e) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorMessage(messageUtil.getMessage(MessageConstants.EMAIL_ALREADY_EXISTS)));
	}

	@ExceptionHandler({ ExpiredJwtException.class })
	public ResponseEntity<Object> handleExpiredJwtException(Exception e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorMessage(messageUtil.getMessage(MessageConstants.SESSION_INVALID)));
	}
}