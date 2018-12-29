package br.com.portal.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.portal.model.User;
import io.jsonwebtoken.Clock;

/**
 * Teste para verificar a classe de criação e validação de tokens jwt
 * @author douglas.takara
 */
@RunWith(JUnit4.class)
public class JwtTokenUtilTest {

	private static final String TEST_USERNAME = "testUser";
	private static final String TEST_EMAIL = "testUser@email.com";

	@Mock
	private Clock clockMock;

	@InjectMocks
	private JwtTokenUtil jwtTokenUtil;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

		ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 3600L);
		ReflectionTestUtils.setField(jwtTokenUtil, "secret", "GLaDOS");
	}

	@Test
	public void shouldGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() throws Exception {
		when(clockMock.now()).thenReturn(DateUtil.yesterday()).thenReturn(DateUtil.now());

		final String token = createToken();
		final String laterToken = createToken();

		assertThat(token).isNotEqualTo(laterToken);
	}

	@Test
	public void shouldgetUsernameFromToken() throws Exception {
		when(clockMock.now()).thenReturn(DateUtil.now());

		final String token = createToken();

		assertThat(jwtTokenUtil.getUsernameFromToken(token)).isEqualTo(TEST_EMAIL);
	}

	@Test
	public void shouldgetCreatedDateFromToken() throws Exception {
		final Date now = DateUtil.now();
		when(clockMock.now()).thenReturn(now);

		final String token = createToken();

		assertThat(jwtTokenUtil.getIssuedAtDateFromToken(token)).isInSameMinuteWindowAs(now);
	}

	@Test
	public void shouldGetExpirationDateFromToken() throws Exception {
		final Date now = DateUtil.now();
		when(clockMock.now()).thenReturn(now);
		final String token = createToken();

		final Date expirationDateFromToken = new Date(jwtTokenUtil.getExpirationDateFromToken(token).getTime());

		assertThat(DateUtil.timeDifference(expirationDateFromToken, now)).isCloseTo(215999667, within(1000L));
	}

	@Test
	public void shouldValidateToken() throws Exception {
		when(clockMock.now()).thenReturn(DateUtil.now());

		String token = createToken();

		User userDetails = mock(User.class);
		when(userDetails.getEmail()).thenReturn(TEST_EMAIL);
		when(userDetails.getToken()).thenReturn(token);

		assertThat(jwtTokenUtil.validateToken(token, userDetails)).isTrue();
	}

	private String createToken() {
		User user = new User();
		user.setName(TEST_USERNAME);
		user.setEmail(TEST_EMAIL);
		String token = jwtTokenUtil.generateToken(user);
		user.setToken(token);
		return token;
	}
}