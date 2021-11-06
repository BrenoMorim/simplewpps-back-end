package br.com.simplewpps.api.service;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Service
public class MockMvcService {

	private MockMvc mock;
	
	public MockMvcService(MockMvc mock) {
		this.mock = mock;
	}
	
	public ResultActions performarGet(URI uri) throws Exception {
		return mock.perform(MockMvcRequestBuilders
				.get(uri)
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
	public ResultActions salvarWallpaper(Long id, String titulo, String descricao, String url, 
										String categoria, String token) throws Exception {
		String json = String.format(
				"{\"titulo\": \"%s\","
			   + "\"url\": \"%s\","
			   + "\"descricao\": \"%s\","
			   + "\"categorias\": [\"%s\"]}", titulo, url, descricao, categoria);
		
		if(id == null) return this.performarPost(new URI("/wpps"), json, token);
		return this.performarPut(new URI("/wpps/" + id), json, token);
	}
}
