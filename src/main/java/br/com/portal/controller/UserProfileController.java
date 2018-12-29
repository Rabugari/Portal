package br.com.portal.controller;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.errors.exceptions.EmailAlreadyExistsException;
import br.com.portal.model.User;
import br.com.portal.service.JwtUserDetailsService;

/**
 * Endpoint para perfil do usuário
 * @author douglas.takara
 */
@RestController
@RequestMapping(path = "/user", 
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE, 
	method = { RequestMethod.GET, RequestMethod.POST })
public class UserProfileController {

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private JwtUserDetailsService userDetailsService;
	
	@PostMapping
	public ResponseEntity<User> save(@RequestBody User user) throws EmailAlreadyExistsException {
		User newUser = userDetailsService.save(user);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getAuthenticatedUser(@PathVariable("id") String id,
			@RequestHeader(value = "Authorization") String token) throws UsernameNotFoundException, AuthenticationException {
		User user = (User) userDetailsService.getById(id, token);
		return ResponseEntity.ok(user);
	}
}