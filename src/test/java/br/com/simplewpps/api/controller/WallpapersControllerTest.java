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
		ResultActions result_del = mock.performarDelete(new URI("/wpps/1"), "");
		result_del.andExpect(MockMvcResultMatchers
				.status()
				.isForbidden());
		
		ResultActions result_put = mock.salvarWallpaper(Long.parseLong("1"), "titulo", "descr", "url", "cat", "");
		result_put.andExpect(MockMvcResultMatchers
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
		
		ResultActions result_put = mock.salvarWallpaper(id, "teste", "teste", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", tokenUser);
		result_put.andExpect(MockMvcResultMatchers
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
		
		ResultActions result = mock.performarDelete(new URI("/wpps/2"), tokenUser);
		result.andExpect(MockMvcResultMatchers
				.status()
				.isForbidden());
		
		ResultActions result_delete = mock.performarDelete(new URI("/wpps/1"), tokenUser);
		result_delete.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		
		ResultActions result_mod = mock.performarDelete(new URI("/wpps/" + id), tokenMod);
		result_mod.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
	}
}
