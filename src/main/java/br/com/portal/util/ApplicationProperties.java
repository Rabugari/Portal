package br.com.portal.util;

/**
 * Constantes para centralizar chamadas ao @{link application.properties}
 * @author douglas.takara
 */
public interface ApplicationProperties {

	final String JWT_SECRET = "${jwt.secret}";
	final String JWT_EXPIRATION = "${jwt.expiration}";
	final String JWT_HEADER = "${jwt.header}"; 
	final String JWT_AUTHENTICATION_PATH = "${jwt.route.authentication.path}";
}
