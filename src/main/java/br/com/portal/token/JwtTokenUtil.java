package br.com.portal.token;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.portal.model.User;
import br.com.portal.util.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

/**
 * Classe para geração e validaçaõ do JWT Token
 * @author douglas.takara
 */
@Component
public class JwtTokenUtil implements Serializable {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "iat";
	private static final long serialVersionUID = -3301605591108950415L;

	private Clock clock = DefaultClock.INSTANCE;

	@Value(ApplicationProperties.JWT_SECRET)
	private String secret;

	@Value(ApplicationProperties.JWT_EXPIRATION)
	private Long expiration;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(clock.now());
	}

	public String generateToken(User userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getEmail());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		User user = (User) userDetails;
		final String email = getUsernameFromToken(token);
		
		return (email.equals(user.getEmail()) && token.equals(user.getToken())) && !isTokenExpired(token);
	}

	/**
	 * 
	 * @param createdDate
	 * @return
	 */
	private Date calculateExpirationDate(Date createdDate) {
		//tempo de expiração calculada em minutos
		return new Date(createdDate.getTime() + expiration * 60 * 1000);
	}
}
