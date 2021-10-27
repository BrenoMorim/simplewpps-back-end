package br.com.simplewpps.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.simplewpps.api.model.Wallpaper;

public interface WallpaperRepository extends JpaRepository<Wallpaper, Long> {

	public Page<Wallpaper> findByTitulo(String titulo, Pageable paginacao);
	
}
