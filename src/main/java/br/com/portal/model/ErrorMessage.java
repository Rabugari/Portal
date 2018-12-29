package br.com.portal.model;

import java.io.Serializable;

/**
 * Dominio para mensagens da aplicação
 * @author douglas.takara
 */
public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 5268758455307540483L;

	private String message;
	
	public ErrorMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}