package br.com.simplewpps.api.service;

import org.springframework.test.web.servlet.ResultActions;

public class DadosRespostaService {

	private MockMvcService mock;

	public DadosRespostaService(MockMvcService mock) {
		this.mock = mock;
	}
	
	public String getToken(String email, String senha) throws Exception {
		String content = mock.efetuarLogin(email, senha)
							.andReturn()
							.getResponse()
							.getContentAsString();
		
		return content.substring(content.indexOf(":") + 1, content.indexOf(","));
	}
	
	public Integer getStatus(ResultActions result) throws Exception {
		return result.andReturn().getResponse().getStatus();
	}
	
	public Boolean verificaSeCorpoContemJson(ResultActions result, String chave, String valor) throws Exception {
		String conteudo = result.andReturn().getResponse().getContentAsString(); 
		
		return (conteudo.contains(String.format("\"%s\":%s", chave, valor)) ||
				conteudo.contains(String.format("\"%s\":\"%s\"", chave, valor)));
	}
	public Boolean verificaSeCorpoContemString(ResultActions result, String conteudo) throws Exception {
		return result.andReturn().getResponse()
				.getContentAsString().contains(conteudo);
	}
}
