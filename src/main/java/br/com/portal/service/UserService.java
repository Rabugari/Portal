package br.com.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.portal.model.User;
import br.com.portal.repository.UserRepository;

/**
 * Serviço para os usuários
 * @author douglas.takara
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User save(User user) {
		return userRepository.save(user);
	}
}
