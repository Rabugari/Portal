package br.com.portal.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.portal.errors.exceptions.AuthenticationException;
import br.com.portal.errors.exceptions.EmailAlreadyExistsException;
import br.com.portal.model.User;
import br.com.portal.repository.UserRepository;
import br.com.portal.token.JwtTokenUtil;
import br.com.portal.util.MessageUtil.MessageConstants;

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
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if(!optionalUser.isPresent()) {
			throw new UsernameNotFoundException(MessageConstants.USER_INVALID);
		}else {
			return optionalUser.get();
		}
	}
	
	public User save(final User user) throws EmailAlreadyExistsException {
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
	
	public User updateToken(final String email) {
		User user = (User) loadUserByUsername(email);
		
		String token = jwtTokenUtil.generateToken(user);
		user.setToken(token);
		user.setModified(LocalDateTime.now());
		user.setLastLogin(LocalDateTime.now());
		User userSaved = userRepository.save(user);
		return userSaved;
	}

	public User getById(final String id, final String token) throws UsernameNotFoundException, AuthenticationException {
		Optional<User> user = userRepository.findById(Long.parseLong(id));
		if(!user.isPresent()) {
			throw new UsernameNotFoundException(MessageConstants.USER_NOT_FOUND);
		}else {
			String requestToken = token.substring(7);
			if(!requestToken.equals(user.get().getToken())) {
				throw new AuthenticationException(MessageConstants.USER_NOT_AUTHORIZED);
			}
			return user.get();
		}
	}
	
	private boolean isUserEmailAlreadyExists(final String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		return optionalUser.isPresent();
	}
}