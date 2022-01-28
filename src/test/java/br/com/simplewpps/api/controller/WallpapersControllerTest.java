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
public class WallpapersControllerTest {

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
	public void naoPermiteAlteracoesSeOUsuarioNaoEstiverLogado() throws Exception {
		ResultActions resultDel = mock.performarDelete(new URI("/wpps/1"), "");
		resultDel.andExpect(MockMvcResultMatchers
				.status()
				.isForbidden());
		
		ResultActions resultPut = mock.salvarWallpaper(Long.parseLong("1"), "titulo", "descr", "url", "cat", "");
		resultPut.andExpect(MockMvcResultMatchers
				.status()
				.isForbidden());
	}
	
	@Test
	public void deveCriarWallpaperQuandoUsuarioEstiverLogado() throws Exception {
		
		ResultActions result_post = mock.salvarWallpaper(null, "wpp legal", "wpp legal", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		result_post.andExpect(MockMvcResultMatchers
				.status()
				.isCreated());
	}
	
	@Test
	public void deveAlterarWallpaperSeUsuarioLogadoForAutor() throws Exception {
		
		Long id = this.mock.criarWallpaperQualquerERetornarId(tokenUser);
		
		ResultActions resultPut = mock.salvarWallpaper(id, "teste", "teste", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		resultPut.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
	}
	
	@Test
	public void somenteAutorOuModeradorPodemDeletarOWallpaper() throws Exception {
		
		Long id = this.mock.criarWallpaperQualquerERetornarId(tokenUser);
		
		ResultActions resultDelUsuario = mock.performarDelete(new URI("/wpps/2"), tokenUser);
		resultDelUsuario.andExpect(MockMvcResultMatchers
				.status()
				.isForbidden());
		
		ResultActions resultDelAutor = mock.performarDelete(new URI("/wpps/1"), tokenUser);
		resultDelAutor.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		
		ResultActions resultDelMod = mock.performarDelete(new URI("/wpps/" + id), tokenMod);
		resultDelMod.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
	}
	
	@Test
	public void dadosDoFormularioDeSalvarWallpaperDevemSerValidos() throws Exception {
		ResultActions resultNomeInvalido = this.mock.salvarWallpaper(null, "", "", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		resultNomeInvalido.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		ResultActions resultUrlInvalido = this.mock.salvarWallpaper(null, "nome valido", "", 
				"linkinvalido", "paisagem", tokenUser);
		resultUrlInvalido.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		ResultActions resultCategoriaInvalida = this.mock.salvarWallpaper(null, "nome valido", "", 
				"https://wallpaperaccess.com/full/2029165.jpg", "categoria que nao existe", tokenUser);
		resultCategoriaInvalida.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void usuarioDeveConseguirCurtirEDescurtirWallpaper() throws Exception {
		
		Long id = this.mock.criarWallpaperQualquerERetornarId(tokenUser);
		
		ResultActions resultCurtir = this.mock.curtirWallpaper(id, tokenUser);
		resultCurtir.andExpect(MockMvcResultMatchers.status().isOk());
		
		ResultActions wppsSalvos = this.mock.retornarWallpapersSalvos(tokenUser);
		wppsSalvos.andExpect(MockMvcResultMatchers.content().string("id:" + id));
		
		ResultActions resultDescurtir = this.mock.descurtirWallpaper(id, tokenUser);
		resultDescurtir.andExpect(MockMvcResultMatchers.status().isOk());
	
		wppsSalvos = this.mock.retornarWallpapersSalvos(tokenUser);
		wppsSalvos.andExpect(MockMvcResultMatchers.content().string("[]"));
	}
}
