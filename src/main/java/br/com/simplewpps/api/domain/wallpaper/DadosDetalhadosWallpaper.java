package br.com.simplewpps.api.domain.wallpaper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import br.com.simplewpps.api.domain.categoria.CategoriaDto;
import lombok.Getter;

@Getter
public class DadosDetalhadosWallpaper {

	private Long id;
	private String titulo;
	private String url;
	private HashSet<CategoriaDto> categorias = new HashSet<CategoriaDto>();
	private LocalDateTime dataCriacao;
	private String nomeAutor;
	
	public DadosDetalhadosWallpaper(Wallpaper wpp) {
		this.id = wpp.getId();
		this.titulo = wpp.getTitulo();
		this.nomeAutor = wpp.getAutor().getNickname();
		this.url = wpp.getUrl();
		this.dataCriacao = wpp.getDataCriacao();
		this.categorias.addAll(wpp.getCategorias().stream().map(CategoriaDto::new).collect(Collectors.toSet()));
	}
	
}
