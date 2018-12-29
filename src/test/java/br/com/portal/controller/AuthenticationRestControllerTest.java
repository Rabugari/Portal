package br.com.portal.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.portal.service.JwtUserDetailsService;
import br.com.portal.token.JwtAuthenticationRequest;
import br.com.portal.token.JwtTokenUtil;

/**
 * Teste para validar o endpoint de autenticação
 * @author douglas.takara
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationRestControllerTest {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Before
	public void setUp(){
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	@Test
	public void errorOnAuthenticationWithNoCredentials() throws JsonGenerationException, JsonMappingException, IOException, Exception {
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest(null, null);
		mvc.perform(post("/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void errorOnAuthenticationWithNpEmail() throws JsonGenerationException, JsonMappingException, IOException, Exception {
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest(null, "password");
		mvc.perform(post("/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void errorOnAuthenticationWithNoPassword() throws JsonGenerationException, JsonMappingException, IOException, Exception {
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest("email", null);
		mvc.perform(post("/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	@WithAnonymousUser
	public void successfulAuthenticationWithAnonymousUser() throws JsonGenerationException, JsonMappingException, IOException, Exception {
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest("email", "password");
		mvc.perform(post("/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))
		.andExpect(status().is2xxSuccessful());
	}
}