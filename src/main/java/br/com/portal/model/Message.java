package br.com.portal.model;

import java.io.Serializable;

/**
 * Dominio para mensagens da aplicação
 * @author douglas.takara
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 5268758455307540483L;

	private String value;
	
	public Message(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
