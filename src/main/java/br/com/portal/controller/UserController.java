package br.com.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.portal.model.User;
import br.com.portal.service.UserService;

@RestController("/perfil")
public class UserController {

	@Autowired
	private UserService service;
	
	@PostMapping
	public ResponseEntity<User> save(@RequestBody User user){
		User newUser = service.save(user);
		return (ResponseEntity<User>) ResponseEntity.ok(newUser).status(HttpStatus.CREATED);
		
	}
}
