package br.com.simplewpps.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Before;
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
public class WallpaperControllerTest {

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
	public void naoPermiteAlteracoesSeOUsuarioNaoEstiverLogado() throws Exception {
		ResultActions resultDel = mock.performarDelete(new URI("/wpps/1"), "");
		assertTrue(service.getStatus(resultDel) >= 400);
		
		ResultActions resultPut = mock.salvarWallpaper(Long.parseLong("1"), "titulo", "url", "cat", "");
		assertTrue(service.getStatus(resultPut) >= 400);
	}
	
	@Test
	public void deveCriarWallpaperQuandoUsuarioEstiverLogado() throws Exception {
		System.out.println(this.tokenUser);
		ResultActions resultPost = mock.salvarWallpaper(null, "wpp legal",
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		assertEquals(Integer.valueOf(201), service.getStatus(resultPost));
	}
	
	@Test
	public void deveAlterarWallpaperSeUsuarioLogadoForAutor() throws Exception {
		
		Long id = this.mock.criarWallpaperQualquerERetornarId(tokenUser);
		
		ResultActions resultPut = mock.salvarWallpaper(id, "teste", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		assertEquals(Integer.valueOf(200), service.getStatus(resultPut));
	}
	
	@Test
	public void somenteAutorOuModeradorPodemDeletarOWallpaper() throws Exception {
		
		Long id = this.mock.criarWallpaperQualquerERetornarId(tokenUser);
		
		ResultActions resultDelUsuario = mock.performarDelete(new URI("/wpps/2"), tokenUser);
		assertEquals(Integer.valueOf(401), service.getStatus(resultDelUsuario));
		
		ResultActions resultDelAutor = mock.performarDelete(new URI("/wpps/1"), tokenUser);
		assertEquals(Integer.valueOf(204), service.getStatus(resultDelAutor));
		
		ResultActions resultDelMod = mock.performarDelete(new URI("/wpps/" + id), tokenMod);
		assertEquals(Integer.valueOf(204), service.getStatus(resultDelMod));
	}
	
	@Test
	public void dadosDoFormularioDeSalvarWallpaperDevemSerValidos() throws Exception {
		ResultActions resultNomeInvalido = this.mock.salvarWallpaper(null, "", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		assertEquals(Integer.valueOf(400), service.getStatus(resultNomeInvalido));
		
		ResultActions resultUrlInvalido = this.mock.salvarWallpaper(null, "nome valido", 
				"linkinvalido", "paisagem", tokenUser);
		assertEquals(Integer.valueOf(400), service.getStatus(resultUrlInvalido));
		
		ResultActions resultCategoriaInvalida = this.mock.salvarWallpaper(null, "nome valido", 
				"https://wallpaperaccess.com/full/2029165.jpg", "categoria que nao existe", tokenUser);
		assertEquals(Integer.valueOf(400), service.getStatus(resultCategoriaInvalida));	
	}
	
	@Test
	public void usuarioDeveConseguirCurtirEDescurtirWallpaper() throws Exception {
		
		Long id = this.mock.criarWallpaperQualquerERetornarId(tokenUser);
		
		ResultActions resultCurtir = this.mock.curtirWallpaper(id, tokenUser);
		assertEquals(Integer.valueOf(204), service.getStatus(resultCurtir));
		
		ResultActions wppsSalvos = this.mock.retornarWallpapersSalvos(tokenUser);
		assertTrue(service.verificaSeCorpoContemJson(wppsSalvos, "id", id.toString()));
		
		ResultActions resultDescurtir = this.mock.descurtirWallpaper(id, tokenUser);
		assertEquals(Integer.valueOf(204), service.getStatus(resultDescurtir));
		
		wppsSalvos = this.mock.retornarWallpapersSalvos(tokenUser);
		assertTrue(service.verificaSeCorpoContemJson(wppsSalvos, "content", "[]"));
	}
}
