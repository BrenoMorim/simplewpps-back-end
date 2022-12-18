package br.com.simplewpps.api.domain.categoria;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CategoriaDto {
	
	private Long id;
	private String nome;
	
	public CategoriaDto(Categoria cat) {
		this.nome = cat.getNome();
		this.id = cat.getId();
	}
	
	public static Page<CategoriaDto> converter(Page<Categoria> categorias) {
		return categorias.map(CategoriaDto::new);
	}
}
