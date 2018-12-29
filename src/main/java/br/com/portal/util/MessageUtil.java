package br.com.portal.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Classe para recuperar as mensagens de acordo com o idioma configurado
 * @author douglas.takara
 */
@Component
public class MessageUtil {

	@Autowired
	private MessageSource messageSource;
	
	@Value("locale")
	private String locale;
	
	public String getMessage(final String messageProperty) {
		return messageSource.getMessage(messageProperty, null, new Locale(locale));
	}
	
	/**
	 * Constantes para centralizar chamadas ao @{link messages.properties}
	 * @author douglas.takara
	 */
	public static interface MessageConstants {
		final String SESSION_INVALID = "session.invalid";
		final String USER_INVALID = "user.invalid";
		final String USER_ERROR = "user.error";
		final String USER_NOT_AUTHORIZED = "user.not_authorized";
		final String USER_NOT_FOUND ="user.not_found";
		final String EMAIL_ALREADY_EXISTS = "user.mail.already_exist";
		final String USER_INSERT_ERROR = "user.inserter.error";
		final String INSERT_AN_USERNAME = "user.insert.username";
		final String INSERT_AN_EMAIL = "user.insert.email";
		final String INSERT_A_PASSWORD = "user.insert.password";
		final String INSERT_A_VALID_EMAIL = "insert.valid.email";
		
	}
}