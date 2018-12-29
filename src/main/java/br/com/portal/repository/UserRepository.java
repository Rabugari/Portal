package br.com.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.portal.model.User;

/**
 * Repositório para o usuário
 * {@link User}
 * @author douglas.takara
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	
	Optional<User> findByToken(String token);
}