package br.com.portal.errors.exceptions;

/**
 * Exceção para inserção de e-mail repetidos
 * @author douglas.takara
 */
public class EmailAlreadyExistsException extends Exception {
	
	private static final long serialVersionUID = -806111550189031563L;

//	public EmailAlreadyExistsException(final String message, final Throwable e) {
//		super(message, e);
//	}

	public EmailAlreadyExistsException() {}
}
