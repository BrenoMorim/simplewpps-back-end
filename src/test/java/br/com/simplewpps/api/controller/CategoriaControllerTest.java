package br.com.simplewpps.api.controller;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
@TestInstance(Lifecycle.PER_CLASS)
public class CategoriaControllerTest {

	@Autowired
	private MockMvcService mock;
	private String tokenUser;
	private String tokenMod;
	
	@BeforeAll
	public void executarLoginMod() throws Exception {
		String content = mock.efetuarLogin("mod@modmail.com", "1234567")
				.andReturn()
				.getResponse()
				.getContentAsString();
		this.tokenMod = content.substring(content.indexOf(":") + 1, content.indexOf(","));
	}
	
	@BeforeAll
	public void executarLoginUsuario() throws Exception {
		String content = mock.efetuarLogin("breno@brenomail.com", "1234567")
				.andReturn()
				.getResponse()
				.getContentAsString();
		this.tokenUser = content.substring(content.indexOf(":") + 1, content.indexOf(","));
	}
	
	@Test
	public void naoDeveSerNecessarioEstarLogadoParaPesquisarCategoria() throws Exception {
		ResultActions result = this.mock.performarGet(new URI("/categorias"));
		result.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void somenteModeradoresPodemAdicionarNovasCategorias() throws Exception {
		ResultActions resultMod = this.mock.salvarCategoria(null, "categoria de teste", tokenMod);
		resultMod.andExpect(MockMvcResultMatchers.status().isCreated());
		
		ResultActions resultUsuario = this.mock.salvarCategoria(null, "categoria de teste 2", tokenUser);
		resultUsuario.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	public void naoPodemExistirCategoriasComNomesIguais() throws Exception {
		
		ResultActions resultPost1 = this.mock.salvarCategoria(null, "teste", tokenMod);
		resultPost1.andExpect(MockMvcResultMatchers.status().isCreated());
		
		ResultActions resultPost2 = this.mock.salvarCategoria(null, "teste", tokenMod);
		resultPost2.andExpect(MockMvcResultMatchers.status().isBadRequest());
		resultPost2.andExpect(MockMvcResultMatchers.content().string("Já existe uma categoria com esse nome!"));
	}

	@Test
	public void nomeDeCategoriaDeveTerEntre4E30Caracteres() throws Exception {
		ResultActions resultCurtoDeMais = this.mock.salvarCategoria(Long.parseLong("1"), "xxx", tokenMod);
		resultCurtoDeMais.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		ResultActions resultLongoDeMais = this.mock.salvarCategoria(Long.parseLong("1"), "abcdefghijklmnopqrstuvwxyz0123456789", tokenMod);
		resultLongoDeMais.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		ResultActions resultTamanhoCorreto = this.mock.salvarCategoria(Long.parseLong("1"), "paisagens", tokenMod);
		resultTamanhoCorreto.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
