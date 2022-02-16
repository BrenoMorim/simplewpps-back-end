package br.com.simplewpps.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Wallpaper {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String url;
	private LocalDateTime dataCriacao = LocalDateTime.now();
	@ManyToOne()
	private Usuario autor;
	@ManyToMany()
	private Collection<Categoria> categorias = new HashSet<Categoria>(); 
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void resetarCategorias() {
		this.categorias.clear();
	}

	public Usuario getAutor() {
		return autor;
	}
	
	public Collection<Categoria> getCategorias() {
		return new ArrayList<Categoria>(this.categorias);
	}
	
	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Long getId() {
		return id;
	}
	
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	
	public void adicionarCategoria(Categoria cat) {
		this.categorias.add(cat);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(autor, id, titulo, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wallpaper other = (Wallpaper) obj;
		return other.getId() == this.getId();
	}
	
}
