package br.com.simplewpps.api.domain.wallpaper;

import java.util.HashSet;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import br.com.simplewpps.api.domain.categoria.Categoria;
import br.com.simplewpps.api.domain.categoria.CategoriaRepository;

public class SalvarWallpaperForm {
	
	@NotBlank
	@Size(min=5, max=30)
	private String titulo;
	@NotBlank @URL
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
