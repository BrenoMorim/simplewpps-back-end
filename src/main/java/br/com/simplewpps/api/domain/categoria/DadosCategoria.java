package br.com.simplewpps.api.domain.categoria;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class DadosCategoria {
	
	private Long id;
	private String nome;
	
	public DadosCategoria(Categoria cat) {
		this.nome = cat.getNome();
		this.id = cat.getId();
	}
	
	public static Page<DadosCategoria> converter(Page<Categoria> categorias) {
		return categorias.map(DadosCategoria::new);
	}
}
