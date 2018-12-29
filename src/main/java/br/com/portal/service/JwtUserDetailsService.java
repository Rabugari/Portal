package br.com.portal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.portal.errors.exceptions.EmailAlreadyExistsException;
import br.com.portal.model.User;
import br.com.portal.repository.UserRepository;
import br.com.portal.token.JwtTokenUtil;

/**
 * Service para validar o usuário
 * {@link User}
 * @author douglas.takara
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if(!optionalUser.isPresent()) {
			throw new UsernameNotFoundException(String.format("not found username '%s'.", optionalUser));
		}else {
			return optionalUser.get();
		}
	}
	
	public User save(User user) throws EmailAlreadyExistsException {
		if(isUserEmailAlreadyExists(user.getEmail())) {
			throw new EmailAlreadyExistsException();
		}
		
		String token = jwtTokenUtil.generateToken(user);
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setToken(token);
		user.setModified(LocalDateTime.now());
		user.setCreated(LocalDateTime.now());
		
		User userSaved = userRepository.save(user);
		return userSaved;
	}
	
	public User updateToken(String email, String token) {
		User user = (User) loadUserByUsername(email);
		user.setToken(token);
		user.setModified(LocalDateTime.now());
		user.setLastLogin(LocalDateTime.now());
		User userSaved = userRepository.save(user);
		return userSaved;
	}
	
	private boolean isUserEmailAlreadyExists(final String email) {
		UserDetails optionalUser = loadUserByUsername(email);
		return optionalUser!=null;
	}

	public List<User> listAll() {
		return userRepository.findAll();
	}
}