package br.com.simplewpps.api.controller.form;

import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import br.com.simplewpps.api.model.Categoria;
import br.com.simplewpps.api.model.Wallpaper;
import br.com.simplewpps.api.repository.CategoriaRepository;

public class SalvarWallpaperForm {
	
	@NotNull @NotEmpty @Size(min=5, max=30)
	private String titulo;
	@NotNull @NotEmpty @URL
	private String url;
	@NotNull
	private List<String> categorias;
	
	public String getTitulo() {
		return titulo;
	}
	public String getUrl() {
		return url;
	}
	public List<String> getCategorias() {
		return this.categorias;
	}
	public HashSet<Categoria> getCategoriasBanco(CategoriaRepository repository) {
		if (this.categorias.size() > 5 || this.categorias.size() < 1) return null;
		
		HashSet<Categoria> set = new HashSet<Categoria>();
		try {
			this.categorias.forEach(s -> {
			Categoria cat = repository.findByNome(s).get();
			set.add(cat);
		});
			return set;
		} catch(RuntimeException e) {
			return null;
		}
	}
	public Wallpaper converter() {
		Wallpaper wpp = new Wallpaper();
		wpp.setTitulo(titulo);
		wpp.setUrl(url);
		return wpp;
	}
}
