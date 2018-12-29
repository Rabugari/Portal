package br.com.portal.token;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.portal.errors.exceptions.AuthenticationException;
import br.com.portal.model.User;
import br.com.portal.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.IOException;

/**
 * Filtro para validar tokens
 * @author douglas.takara
 */
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final JwtUserDetailsService userDetailsService;
	private final JwtTokenUtil jwtTokenUtil;
	private final String tokenHeader;

	public JwtAuthorizationTokenFilter(JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil,
			@Value("${jwt.header}") String tokenHeader) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.tokenHeader = tokenHeader;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException, java.io.IOException {
		logger.debug("Validando autenticação '{}'", request.getRequestURL());

		final String requestHeader = request.getHeader(this.tokenHeader);

		String username = null;
		String authToken = null;
		if (requestHeader != null && requestHeader.startsWith(BEARER)) {
			authToken = requestHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			} catch (IllegalArgumentException e) {
				logger.error("Erro ao validar o usuário", e);
			} catch (ExpiredJwtException e) {
				logger.warn("Token expirado.", e);
			} catch(MalformedJwtException e) {
				logger.warn("Token inválido", e);
				throw new AuthenticationException("user.not_authorized", e);
//				response.getSendError(HttpStatus.UNAUTHORIZED.value(), "{user.not_authorized}");
			}
		} else {
			 logger.warn("Acesso endpoint de exceção ou Tipo de 'Authorization' inválida.");
		}

		 logger.debug("Verificando as credenciais do usuário '{}'", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User userDetails = (User) this.userDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				 logger.info("Usuário autenticado, inserindo no security context", username);
				 
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}