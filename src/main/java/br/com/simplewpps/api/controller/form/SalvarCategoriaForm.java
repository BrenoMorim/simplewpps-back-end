package br.com.simplewpps.api.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import br.com.simplewpps.api.model.Categoria;

public class SalvarCategoriaForm {

	@NotBlank
	@Size(min=4, max=30)
	private String nome;
	
	public String getNome() {
		return nome;
	}
	
	public Categoria converter() {
		Categoria cat = new Categoria();
		cat.setNome(this.nome); 
		return cat;
	}
}
