package br.com.simplewpps.api.controller;

import java.net.URI;

import org.junit.jupiter.api.Test;
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
public class WallpapersControllerTest {

	@Autowired
	private MockMvcService mock;
	
	@Test
	public void deveRetornarJsonDetalhadoQuandoIdForPassadoNaURL() throws Exception {
		ResultActions result = mock.performarGet(new URI("/wpps/1"));
		result.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
		result.andExpect(MockMvcResultMatchers
				.content()
				.json("{\r\n"
						+ "    \"id\": 1,\r\n"
						+ "    \"titulo\": \"wpp legal\",\r\n"
						+ "    \"descricao\": \"wpp bem legal\",\r\n"
						+ "    \"categorias\": [\r\n"
						+ "        {\r\n"
						+ "            \"nome\": \"paisagem\"\r\n"
						+ "        }\r\n"
						+ "    ],\r\n"
						+ "    \"dataCriacao\": null,\r\n"
						+ "    \"nomeAutor\": \"breno\"\r\n"
						+ "}"));
	}
	
}
