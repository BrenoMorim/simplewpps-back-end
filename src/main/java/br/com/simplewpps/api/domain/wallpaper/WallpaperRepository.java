package br.com.simplewpps.api.domain.wallpaper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallpaperRepository extends JpaRepository<Wallpaper, Long> {

	public Page<Wallpaper> findByTitulo(String titulo, Pageable paginacao);
	
	public Page<Wallpaper> findByTituloAndCategoriasNome(String titulo, String categoriaNome, Pageable paginacao);
	
	public Page<Wallpaper> findByCategoriasNome(String categoriaNome, Pageable paginacao);
}
