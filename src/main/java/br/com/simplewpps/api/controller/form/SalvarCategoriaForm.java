package br.com.simplewpps.api.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.simplewpps.api.model.Categoria;

public class SalvarCategoriaForm {

	@NotNull @NotEmpty @Size(min=4, max=30)
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
