package br.com.portal.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import javax.transaction.Transactional;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.portal.model.User;
import br.com.portal.repository.UserRepository;
import br.com.portal.service.JwtUserDetailsService;
import br.com.portal.token.JwtTokenUtil;

/**
 * Teste para validar o endpoint de perfil do usuário
 * @author douglas.takara
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class UserProfileControllerTest {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@MockBean
	private JwtUserDetailsService userDetailsService;

	@MockBean
	private UserRepository userRepository;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	public void shouldReturnBadRequestBecauseUserIsNull()
			throws JsonGenerationException, JsonMappingException, IOException, Exception {
		User newUser = new User();
		this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newUser))).andExpect(status().is4xxClientError());
	}

	@Test
	public void shouldReturnBadRequestBecauseUsernameIsNull()
			throws JsonGenerationException, JsonMappingException, IOException, Exception {
		User newUser = new User();
		newUser.setEmail("user@email");
		newUser.setPassword("password");
		this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newUser))).andExpect(status().is4xxClientError());
	}

	@Test
	public void shouldReturnBadRequestBecauseEmailIsNull()
			throws JsonGenerationException, JsonMappingException, IOException, Exception {
		User newUser = new User();
		newUser.setName("user2");
		newUser.setPassword("password");
		this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newUser))).andExpect(status().is4xxClientError());
	}

	@Test
	public void shouldReturnBadRequestBecausePasswordIsNull()
			throws JsonGenerationException, JsonMappingException, IOException, Exception {
		User newUser = new User();
		newUser.setName("user2");
		newUser.setEmail("user@email");
		this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newUser))).andExpect(status().is4xxClientError());
	}

	@Test
	@Rollback(false)
	public void shouldAuthorizedEndpointExceptionOnRegisterUser()
			throws JsonGenerationException, JsonMappingException, IOException, Exception {
		User newUser = new User();
		newUser.setName("userRollback");
		newUser.setEmail("userRollback@email");
		newUser.setPassword("userRollback");

		when(userRepository.save(newUser)).thenReturn(newUser);

		this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newUser))).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void shouldNotAuthorizedWithoutToken() throws Exception {
		this.mvc.perform(get("/user/1")).andExpect(status().is4xxClientError());
	}
}