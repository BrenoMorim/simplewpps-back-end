package br.com.simplewpps.api.service;

import java.net.URI;

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
				.contentType(MediaType.APPLICATION_JSON)
			);
	}
	
	public ResultActions performarPost(URI uri, String json) throws Exception {
		return mock.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
			);
	}
	
	public ResultActions efetuarLogin(String email, String senha) throws Exception {
		String json = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);
		
		return this.performarPost(new URI("/auth/login"), json);
	}
	public ResultActions efetuarRegister(String nickname, String email, String senha) throws Exception {
		String json = String.format("{\"nickname\":\"%s\", \"email\":\"%s\", \"senha\":\"%s\"}", 
					nickname, email, senha);
	
		return this.performarPost(new URI("/auth/register"), json);
	}
}
