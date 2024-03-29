package br.com.simplewpps.api.domain.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.simplewpps.api.domain.wallpaper.Wallpaper;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);
	
	@Query("SELECT u.wppsSalvos FROM Usuario u WHERE u.id = ?1")
	public Page<Wallpaper> getWallpapersSalvos(Long usuarioId, Pageable paginacao);
	
}
