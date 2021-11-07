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
		
		Long id = Long.valueOf(this.mock.salvarWallpaper(null, "wpp legal", "wpp legal", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser)
				.andReturn()
				.getResponse()
				.getHeader("Location")
				.split("/")[4]);
		
		ResultActions resultPut = mock.salvarWallpaper(id, "teste", "teste", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		resultPut.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
	}
	
	@Test
	public void somenteAutorOuModeradorPodemDeletarOWallpaper() throws Exception {
		
		Long id = Long.valueOf(this.mock.salvarWallpaper(null, "wpp legal", "wpp legal", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser)
				.andReturn()
				.getResponse()
				.getHeader("Location")
				.split("/")[4]);
		
		ResultActions resultUsuario = mock.performarDelete(new URI("/wpps/2"), tokenUser);
		resultUsuario.andExpect(MockMvcResultMatchers
				.status()
				.isForbidden());
		
		ResultActions resultDel = mock.performarDelete(new URI("/wpps/1"), tokenUser);
		resultDel.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		
		ResultActions resultMod = mock.performarDelete(new URI("/wpps/" + id), tokenMod);
		resultMod.andExpect(MockMvcResultMatchers
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
}
