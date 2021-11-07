package br.com.simplewpps.api.controller.dto;

import org.springframework.data.domain.Page;

import br.com.simplewpps.api.model.Categoria;

public class CategoriaDto {
	
	private Long id;
	private String nome;
	
	public CategoriaDto(Categoria cat) {
		this.nome = cat.getNome();
		this.id = cat.getId();
	}
	
	public String getNome() {
		return nome;
	}
	
	public Long getId() {
		return id;
	}
	
	public static Page<CategoriaDto> converter(Page<Categoria> categorias) {
		return categorias.map(CategoriaDto::new);
	}
}
