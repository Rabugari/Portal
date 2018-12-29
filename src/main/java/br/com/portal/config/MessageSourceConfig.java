package br.com.portal.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuração para mensagens da aplicação
 * @author douglas.takara
 */
@Configuration
public class MessageSourceConfig {

	private static final String BASE_NAME = "classpath:messages";
	private static final String ENCODING = "UTF-8";

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(BASE_NAME);
		messageSource.setDefaultEncoding(ENCODING);
		return messageSource;
	}
	
	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		return localValidatorFactoryBean;
	}
}