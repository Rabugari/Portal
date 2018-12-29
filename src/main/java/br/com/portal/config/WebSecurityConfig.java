package br.com.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.portal.service.JwtUserDetailsService;
import br.com.portal.token.JwtAuthenticationEntryPoint;
import br.com.portal.token.JwtAuthorizationTokenFilter;
import br.com.portal.util.ApplicationProperties;

/**
 * Configuração para acesso das apis
 * @author douglas.takara
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private JwtAuthorizationTokenFilter authenticationTokenFilter;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Value(ApplicationProperties.JWT_HEADER)
	private String tokenHeader;
	
	@Value(ApplicationProperties.JWT_AUTHENTICATION_PATH)
	private String authenticationPath;
	
	private static final String[] SWAGGER_PATHS = { "/v2/api-docs/**", "/swagger-ui.html/**",
			    "/webjars/**", "/swagger-resources/**" };
	
	/**
	 * Configurando service para usuário e tipo de encode
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService)
			.passwordEncoder(passwordEncoderBean());
	}
	
	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Configurando segurança de acesso dos usuário 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/user/**").permitAll()
			.antMatchers(SWAGGER_PATHS).permitAll()
			.anyRequest().authenticated();
		
		http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		//desabilitando cache de página
		http.headers().frameOptions().sameOrigin().cacheControl();
	}
	
	/**
	 * Configurando segurança de acesso dos usuário 
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(HttpMethod.POST, authenticationPath)
			.antMatchers(HttpMethod.POST, "/user/**")
			.antMatchers(SWAGGER_PATHS);
	}
}