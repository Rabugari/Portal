package br.com.portal.util;

import org.springframework.beans.factory.annotation.Value;

import br.com.portal.model.User;

public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	public User parseToken(String token) {
		try {
			Claims body = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
					
			//https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
			User u = new User("", "", "", null);
			return u;
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	 /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     * 
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(User u) {
        Claims claims = Jwts.claims().setSubject(u.getUsername());
        claims.put("userId", u.getId() + "");
        claims.put("role", u.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
