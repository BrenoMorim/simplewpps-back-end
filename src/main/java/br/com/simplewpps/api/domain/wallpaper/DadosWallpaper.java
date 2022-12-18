package br.com.simplewpps.api.domain.wallpaper;

import org.springframework.data.domain.Page;

public record DadosWallpaper(String titulo, Long id, String url) {
	public DadosWallpaper(Wallpaper wpp) {
		this(wpp.getTitulo(), wpp.getId(), wpp.getUrl());
	}

	public static Page<DadosWallpaper> converter(Page<Wallpaper> topicos) {
		return topicos.map(DadosWallpaper::new);
	}
}
