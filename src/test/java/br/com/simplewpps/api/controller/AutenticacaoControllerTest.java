package br.com.simplewpps.api.controller;


import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.simplewpps.api.SimplewppsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SimplewppsApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AutenticacaoControllerTest {

	@Autowired
	private MockMvc mock;
	
	public URI getURIRegister() throws Exception {
		return new URI("/auth/register");
	}

	public URI getURILogin() throws Exception {
		return new URI("/auth/login");
	}
	
	public ResultActions performarPost(URI uri, String json) throws Exception {
		return mock.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
			);
	}
	
	@Test
	public void deveRetornar400CasoDadosDeAutenticacaoEstejamErrados() throws Exception {
		String json = "{\"nickname\":\"na\", \"email\":\"emailinvalido.com\", \"senha\":\"123\"}";
		
		ResultActions result = performarPost(getURIRegister(), json);
		result.andExpect(MockMvcResultMatchers
					.status()
					.is(400));
	}
	@Test
	public void deveCriarUsuarioCasoDadosDeRegistroEstejamCorretos() throws Exception {
		String json = "{\"nickname\":\"nome_valido\", \"email\":\"emailcorreto@email.com\", \"senha\":\"1234567\"}";
		
		ResultActions result = performarPost(getURIRegister(), json);
		result.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		result.andExpect(MockMvcResultMatchers
				.content()
				.json("{\"nickname\":\"nome_valido\","
						+ " \"email\":\"emailcorreto@email.com\"}"));
		
	}
	@Test
	public void naoDevePermitirQueDoisUsuariosTenhamOMesmoEmail() throws Exception {
		String json = "{\"nickname\":\"nome_valido\","
				+ " \"email\":\"breno@brenomail.com\","
				+ " \"senha\":\"1234567\"}";
		
		ResultActions result = performarPost(getURIRegister(), json);
		result.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
		result.andExpect(MockMvcResultMatchers
				.content()
				.string("Já existe um usuário com este email!"));
		
	}
	@Test
	public void deveRetornarTokenComDadosDeLoginValidos() throws Exception {
		String json = "{\"email\":\"breno@brenomail.com\", \"senha\":\"1234567\"}";
		
		ResultActions result = performarPost(getURILogin(), json);
		result.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		result.andExpect(MockMvcResultMatchers
				.content()
				.json("{\"tipo\":\"Bearer\"}"));
		
	}
	@Test
	public void deveRetornar400ComDadosDeLoginInvalidos() throws Exception {
		String json = "{\"email\":\"email@emailinvalido.com\","
				+ " \"senha\":\"1234567\"}";
		
		ResultActions result = performarPost(getURILogin(), json);
		result.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
}