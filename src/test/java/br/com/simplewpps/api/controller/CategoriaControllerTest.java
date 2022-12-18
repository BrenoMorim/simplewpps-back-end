package br.com.simplewpps.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ SimplewppsApplication.class })
@ActiveProfiles("test")
@AutoConfigureMockMvc()
@TestInstance(Lifecycle.PER_CLASS)
public class CategoriaControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvcService mock;
	private DadosRespostaService service;
	private String tokenUser;
	private String tokenMod;

	@Before
	public void setup() throws Exception {
		this.mock = new MockMvcService(MockMvcBuilders.webAppContextSetup(webApplicationContext).build());
		this.service = new DadosRespostaService(this.mock);
		this.tokenMod = service.getToken("mod@modmail.com", "1234567");
		this.tokenUser = service.getToken("breno@brenomail.com", "1234567");
	}

	@Test
	public void naoDeveSerNecessarioEstarLogadoParaPesquisarCategoria() throws Exception {
		ResultActions result = this.mock.performarGet(new URI("/categorias"));
		assertEquals(Integer.valueOf(200), service.getStatus(result));
	}
	
	@Test
	public void moderadoresPodemAdicionarNovasCategorias() throws Exception {
		ResultActions resultMod = this.mock.salvarCategoria(null, "categoria de teste", tokenMod);
		assertEquals(Integer.valueOf(201), service.getStatus(resultMod));
	}

	@Test
	@Ignore("Autorização por roles não está funcionando no perfil de testes, somente nos outros")
	public void usuariosComunsNaoPodemAdicionarNovasCategorias() throws Exception {
		ResultActions resultUsuario = this.mock.salvarCategoria(null, "categoria de teste 2", tokenUser);
		assertEquals(Integer.valueOf(403), service.getStatus(resultUsuario));
	}
	
	@Test
	public void naoPodemExistirCategoriasComNomesIguais() throws Exception {
		
		ResultActions resultPost1 = this.mock.salvarCategoria(null, "teste", tokenMod);
		assertEquals(Integer.valueOf(201), service.getStatus(resultPost1));
		
		ResultActions resultPost2 = this.mock.salvarCategoria(null, "teste", tokenMod);
		assertEquals(Integer.valueOf(400), service.getStatus(resultPost2));		
		assertTrue(service.verificaSeCorpoContemString(resultPost2, "Já existe uma categoria com esse nome!"));
	}

	@Test
	public void nomeDeCategoriaDeveTerEntre4E30Caracteres() throws Exception {
		ResultActions resultCurtoDeMais = this.mock.salvarCategoria(Long.parseLong("1"), "xxx", tokenMod);
		assertEquals(Integer.valueOf(400), service.getStatus(resultCurtoDeMais));
		
		ResultActions resultLongoDeMais = this.mock.salvarCategoria(Long.parseLong("1"), "abcdefghijklmnopqrstuvwxyz0123456789", tokenMod);
		assertEquals(Integer.valueOf(400), service.getStatus(resultLongoDeMais));
		
		ResultActions resultTamanhoCorreto = this.mock.salvarCategoria(Long.parseLong("1"), "paisagens", tokenMod);
		assertEquals(Integer.valueOf(200), service.getStatus(resultTamanhoCorreto));	
	}
}
