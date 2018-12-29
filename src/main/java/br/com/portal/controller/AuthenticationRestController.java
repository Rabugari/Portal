package br.com.portal.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.exception.AuthenticationException;
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

	// @RequestMapping(value = "${jwt.route.authentication.refresh}", method =
	// RequestMethod.GET)
	// public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest
	// request) {
	// String authToken = request.getHeader(tokenHeader);
	// final String token = authToken.substring(7);
	// String username = jwtTokenUtil.getUsernameFromToken(token);
	// User user = (User) userDetailsService.loadUserByUsername(username);
	//
	// ZonedDateTime zdt = user.getLastLogin().atZone(ZoneId.systemDefault());
	//
	// if (jwtTokenUtil.canTokenBeRefreshed(token, Date.from(zdt.toInstant()))) {
	// return ResponseEntity.ok(user);
	// } else {
	// return ResponseEntity.badRequest().body(null);
	// }
	// }

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
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
			throw new AuthenticationException("User is disabled!", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Bad credentials!", e);
		}
	}
}