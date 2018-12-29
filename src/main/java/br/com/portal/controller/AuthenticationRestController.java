package br.com.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.errors.exceptions.AuthenticationException;
import br.com.portal.model.User;
import br.com.portal.service.JwtUserDetailsService;
import br.com.portal.token.JwtAuthenticationRequest;
import br.com.portal.util.ApplicationProperties;
import br.com.portal.util.MessageUtil.MessageConstants;

/**
 * Endpoint para autenticação do usuário
 * @author douglas.takara
 */
@RestController
@RequestMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {
		RequestMethod.GET, RequestMethod.POST })
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private JwtUserDetailsService userDetailsService;
	
	@PostMapping(value = ApplicationProperties.JWT_AUTHENTICATION_PATH)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws Exception {
		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		userDetailsService.updateToken(authenticationRequest.getEmail());
		final User userDetails = (User) userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		return ResponseEntity.ok(userDetails);
	}

	/**
	 * Valida as credenciais do usuário
	 */
	private void authenticate(final String username, final String password) {
		try {
			if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				throw new AuthenticationException(MessageConstants.USER_INVALID);
			}
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException(MessageConstants.USER_INVALID, e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException(MessageConstants.USER_INVALID, e);
		}
	}
}