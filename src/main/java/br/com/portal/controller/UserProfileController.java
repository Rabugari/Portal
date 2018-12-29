package br.com.portal.controller;

import javax.validation.Valid;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.errors.exceptions.EmailAlreadyExistsException;
import br.com.portal.model.User;
import br.com.portal.service.JwtUserDetailsService;
import io.swagger.annotations.ApiOperation;

/**
 * Endpoint para perfil do usuário
 * @author douglas.takara
 */
@RestController
@RequestMapping(path = "/user", 
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProfileController {

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private JwtUserDetailsService userDetailsService;
	
	/**
	 * Salva o usuário enviado
	 * @param user
	 * @return
	 * @throws EmailAlreadyExistsException
	 */
	@ApiOperation(value="Endpoint para registar um novo do usuário", response=User.class)
	@PostMapping
	public ResponseEntity<User> save(@Valid @RequestBody User user) throws EmailAlreadyExistsException {
		User newUser = userDetailsService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	/**
	 * Retorna o perfil do usuário de acordo com o id
	 * @param id - id do usuário
	 * @param token - access token bearer jwt
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws AuthenticationException
	 */
	@ApiOperation(value="Endpoint para buscar um perfil de um usuário", response=User.class, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{id}",  consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getAuthenticatedUser(@PathVariable("id") String id,
			@RequestHeader(value = "Authorization") String token,
			@RequestHeader(value = "Content-Type") String contentType) throws UsernameNotFoundException, AuthenticationException {
		User user = (User) userDetailsService.getById(id, token);
		return ResponseEntity.ok(user);
	}
}