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
}