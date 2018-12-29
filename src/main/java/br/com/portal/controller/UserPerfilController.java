package br.com.portal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.model.User;
import br.com.portal.service.JwtUserDetailsService;
import br.com.portal.token.JwtTokenUtil;
import br.com.portal.util.MessageUtil;

/**
 * Endpoint para perfil do usuário
 * @author douglas.takara
 */
@RestController
@RequestMapping(path = "/perfil", 
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE, 
	method = { RequestMethod.GET, RequestMethod.POST })
public class UserPerfilController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private MessageUtil messageUtil;

	@PostMapping
	public ResponseEntity<User> save(@RequestBody User user) {
		User newUser = userDetailsService.save(user);
		return ResponseEntity.ok(newUser);
	}

	@GetMapping(value = "/user")
	public User getAuthenticatedUser(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader).substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		User user = (User) userDetailsService.loadUserByUsername(username);
		return user;
	}

	@GetMapping(value = "/all-user")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userDetailsService.listAll());
	}
	
	@GetMapping(value="/message")
	public ResponseEntity<?> getMessage(){
		return ResponseEntity.ok(messageUtil.getMessage("user.invalid"));
	}
}