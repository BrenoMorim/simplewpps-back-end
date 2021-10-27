package br.com.simplewpps.api.controller.dto;

import br.com.simplewpps.api.model.Categoria;

public class CategoriaDto {
	
	private String nome;
	
	public CategoriaDto(Categoria cat) {
		this.nome = cat.getNome();
	}
	
	public String getNome() {
		return nome;
	}
	
}
