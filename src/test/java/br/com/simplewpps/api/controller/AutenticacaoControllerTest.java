package br.com.simplewpps.api.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import br.com.simplewpps.api.SimplewppsApplication;
import br.com.simplewpps.api.service.DadosRespostaService;
import br.com.simplewpps.api.service.MockMvcService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SimplewppsApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AutenticacaoControllerTest {

	@Autowired
	private MockMvcService mock;
	@Autowired
	private DadosRespostaService service;
	
	@Test
	public void deveRetornar400CasoDadosDeAutenticacaoEstejamErrados() throws Exception {
		ResultActions result = mock.efetuarRegister("na", "emailinvalido.com", "123");
		assertEquals(Integer.valueOf(400), service.getStatus(result));	
	}
	@Test
	public void deveCriarUsuarioCasoDadosDeRegistroEstejamCorretos() throws Exception {
		ResultActions result = mock.efetuarRegister("nome_valido", "emailcorreto@email.com", "1234567");
		assertEquals(Integer.valueOf(201), service.getStatus(result));
		assertTrue(service.verificaSeCorpoContemJson(result, "nickname", "nome_valido"));
		assertTrue(service.verificaSeCorpoContemJson(result, "email", "emailcorreto@email.com"));
	}
	@Test
	public void naoDevePermitirQueDoisUsuariosTenhamOMesmoEmail() throws Exception {
		ResultActions result = mock.efetuarRegister("nome_valido", "breno@brenomail.com", "1234567");
		assertEquals(Integer.valueOf(400), service.getStatus(result));
		assertTrue(service.verificaSeCorpoContemString(result, "Já existe um usuário com este email!"));
	}
	@Test
	public void deveRetornarTokenComDadosDeLoginValidos() throws Exception {		
		ResultActions result = mock.efetuarLogin("breno@brenomail.com", "1234567");
		assertEquals(Integer.valueOf(200), service.getStatus(result));
		assertTrue(service.verificaSeCorpoContemJson(result, "tipo", "Bearer"));
		
	}
	@Test
	public void deveRetornar400ComDadosDeLoginInvalidos() throws Exception {		
		ResultActions result = mock.efetuarLogin("email@emailinvalido.com", "1234567");
		assertEquals(Integer.valueOf(400), service.getStatus(result));

	}
}