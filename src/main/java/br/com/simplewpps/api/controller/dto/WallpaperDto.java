package br.com.simplewpps.api.controller.dto;

import org.springframework.data.domain.Page;

import br.com.simplewpps.api.model.Wallpaper;

public class WallpaperDto {
	
	private String titulo;
	private Long id;
	private String url;
	
	public WallpaperDto(Wallpaper wpp) {
		this.titulo = wpp.getTitulo();
		this.id = wpp.getId();
		this.url = wpp.getUrl();
	}
	
	public String getTitulo() {
		return titulo;
	}
	public Long getId() {
		return id;
	}
	public String getUrl() {
		return url;
	}
	public static Page<WallpaperDto> converter(Page<Wallpaper> topicos) {
		return topicos.map(WallpaperDto::new);
	}
}
