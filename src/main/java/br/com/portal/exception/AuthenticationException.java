package br.com.portal.exception;

/**
 * Exception para a autenticação
 * @author douglas.takara
 */
@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {
	
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}