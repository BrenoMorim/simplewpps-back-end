package br.com.simplewpps.api.controller;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.simplewpps.api.SimplewppsApplication;
import br.com.simplewpps.api.service.MockMvcService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SimplewppsApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AutenticacaoControllerTest {

	@Autowired
	private MockMvcService mock;
	
	@Test
	public void deveRetornar400CasoDadosDeAutenticacaoEstejamErrados() throws Exception {
		ResultActions result = mock.efetuarRegister("na", "emailinvalido.com", "123");
		result.andExpect(MockMvcResultMatchers
					.status()
					.is(400));
	}
	@Test
	public void deveCriarUsuarioCasoDadosDeRegistroEstejamCorretos() throws Exception {
		ResultActions result = mock.efetuarRegister("nome_valido", "emailcorreto@email.com", "1234567");
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
		ResultActions result = mock.efetuarRegister("nome_valido", "breno@brenomail.com", "1234567");
		result.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
		result.andExpect(MockMvcResultMatchers
				.content()
				.string("Já existe um usuário com este email!"));
		
	}
	@Test
	public void deveRetornarTokenComDadosDeLoginValidos() throws Exception {		
		ResultActions result = mock.efetuarLogin("breno@brenomail.com", "1234567");
		result.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		result.andExpect(MockMvcResultMatchers
				.content()
				.json("{\"tipo\":\"Bearer\"}"));
		
	}
	@Test
	public void deveRetornar400ComDadosDeLoginInvalidos() throws Exception {		
		ResultActions result = mock.efetuarLogin("email@emailinvalido.com", "1234567");
		result.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
}