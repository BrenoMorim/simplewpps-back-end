package br.com.simplewpps.api.controller.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import br.com.simplewpps.api.model.Wallpaper;

public class DetailedWallpaperDto {

	private Long id;
	private String titulo;
	private String descricao;
	private HashSet<CategoriaDto> categorias = new HashSet<CategoriaDto>();
	private LocalDateTime dataCriacao;
	private String nomeAutor;
	
	public DetailedWallpaperDto(Wallpaper wpp) {
		this.id = wpp.getId();
		this.titulo = wpp.getTitulo();
		this.descricao = wpp.getDescricao();
		this.nomeAutor = wpp.getAutor().getNickname();
		this.dataCriacao = wpp.getDataCriacao();
		this.categorias.addAll(wpp.getCategorias().stream().map(CategoriaDto::new).collect(Collectors.toSet()));
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public HashSet<CategoriaDto> getCategorias() {
		return categorias;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}
	
	
}
