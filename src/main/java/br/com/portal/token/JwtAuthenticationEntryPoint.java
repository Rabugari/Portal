package br.com.portal.token;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import br.com.portal.model.ErrorMessage;
import br.com.portal.util.MessageUtil;

/**
 * Entrypoint para validar acesso sem token
 * @author douglas.takara
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 648337202907888819L;
	private static final String USER_NOT_AUTHORIZED = "user.not_authorized";
	
	@Autowired
	private MessageUtil messageUtil;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		//em caso do usuário tentar acessar o serviço sem nenhuma credencial
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		String json = new Gson().toJson(new ErrorMessage(messageUtil.getMessage(USER_NOT_AUTHORIZED)));
        writer.println(json);
	}
}