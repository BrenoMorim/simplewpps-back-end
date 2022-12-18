package br.com.simplewpps.api.service;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockMvcService {

	private MockMvc mock;

	public MockMvcService(MockMvc mockMvc) {
		this.mock = mockMvc;
	}

	public ResultActions performarGet(URI uri) throws Exception {
		return mock.perform(MockMvcRequestBuilders
				.get(uri)
			);
	}
	
	public ResultActions performarGetComToken(URI uri, String token) throws Exception {
		String autorizacao = String.format("Bearer %s", token.replaceAll("\"", ""));
		return mock.perform(MockMvcRequestBuilders
				.get(uri)
				.header(HttpHeaders.AUTHORIZATION, autorizacao)
			);
	}
	
	public ResultActions performarPut(URI uri, String json, String token) throws Exception {
		String autorizacao = String.format("Bearer %s", token.replaceAll("\"", ""));
		return mock.perform(MockMvcRequestBuilders
				.put(uri)
				.header(HttpHeaders.AUTHORIZATION, autorizacao)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
			);
	}
	
	public ResultActions performarDelete(URI uri, String token) throws Exception {
		String autorizacao = String.format("Bearer %s", token.replaceAll("\"", ""));
		return mock.perform(MockMvcRequestBuilders
				.delete(uri)
				.header(HttpHeaders.AUTHORIZATION, String.format(autorizacao))
			);
	}
	
	public ResultActions performarPost(URI uri, String json, String token) throws Exception {
		String autorizacao = String.format("Bearer %s", token.replaceAll("\"", ""));
		return mock.perform(MockMvcRequestBuilders
				.post(uri)
				.header(HttpHeaders.AUTHORIZATION, autorizacao)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
			);
	}
	
	public ResultActions efetuarLogin(String email, String senha) throws Exception {
		String json = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);
		
		return this.performarPost(new URI("/auth/login"), json, "");
	}
	public ResultActions efetuarRegister(String nickname, String email, String senha) throws Exception {
		String json = String.format("{\"nickname\":\"%s\", \"email\":\"%s\", \"senha\":\"%s\"}", 
					nickname, email, senha);

		return this.performarPost(new URI("/auth/register"), json, "");
	}
	public ResultActions salvarWallpaper(Long id, String titulo, String url, 
										String categoria, String token) throws Exception {
		String json = String.format(
				"{\"titulo\": \"%s\","
			   + "\"url\": \"%s\","
			   + "\"categorias\": [\"%s\"]}", 
			   titulo, url, categoria);
		
		if(id == null) return this.performarPost(new URI("/wpps"), json, token);
		return this.performarPut(new URI("/wpps/" + id), json, token);
	}
	public ResultActions salvarCategoria(Long id, String nome, String token) throws Exception {
		String json = String.format("{\"nome\": \"%s\"}", nome);
		
		if(id == null) return this.performarPost(new URI("/categorias"), json, token);
		return this.performarPut(new URI("/categorias/" + id), json, token);
	}
	
	public ResultActions curtirWallpaper(Long wppId, String token) throws Exception {
		
		return this.performarGetComToken(new URI("/wpps/curtir/" + wppId), token);
		
	}
	
	public ResultActions descurtirWallpaper(Long wppId, String token) throws Exception {
		
		return this.performarGetComToken(new URI("/wpps/descurtir/" + wppId), token);
		
	}
	
	public ResultActions retornarWallpapersSalvos(String token) throws Exception {
		
		return this.performarGetComToken(new URI("/wpps/salvos"), token);
		
	}
	
	public Long criarWallpaperQualquerERetornarId(String token) throws Exception {
		return Long.valueOf(this.salvarWallpaper(null, "wpp legal", 
				"https://wallpaperaccess.com/full/2029165.jpg", "paisagem", token)
				.andReturn()
				.getResponse()
				.getHeader("Location")
				.split("/")[4]);
	}
}
