package br.com.simplewpps.api.domain.wallpaper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import br.com.simplewpps.api.domain.categoria.Categoria;
import br.com.simplewpps.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Wallpaper")
@Table(name = "wallpapers")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Wallpaper {

	@Id
	private Long id = Long.valueOf(LocalDateTime.now().hashCode());
	private String titulo;
	private String url;
	private LocalDateTime dataCriacao = LocalDateTime.now();
	@ManyToOne()
	private Usuario autor;
	@ManyToMany()
	private Collection<Categoria> categorias = new HashSet<Categoria>();
	
	public void resetarCategorias() {
		this.categorias.clear();
	}
	
	public Collection<Categoria> getCategorias() {
		return new ArrayList<Categoria>(this.categorias);
	}
	
	public void adicionarCategoria(Categoria cat) {
		this.categorias.add(cat);
	}
	
}
