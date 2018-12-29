package br.com.portal.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.errors.exceptions.AuthenticationException;
import br.com.portal.model.User;
import br.com.portal.token.JwtAuthenticationRequest;

/**
 * Endpoint para autenticação
 * @author douglas.takara
 */
@RestController
@RequestMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {
		RequestMethod.GET, RequestMethod.POST })
public class AuthenticationRestController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@PostMapping(value = "${jwt.route.authentication.path}")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws Exception {
		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		final User userDetails = (User) userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		return ResponseEntity.ok(userDetails);
	}

	/**
	 * Valida a autenticação do usuário
	 */
	private void authenticate(String username, String password) {
		try {
			Objects.requireNonNull(username);
			Objects.requireNonNull(password);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("user.invalid", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("user.invalid", e);
		}
	}
}