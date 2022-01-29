package br.com.simplewpps.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.simplewpps.api.model.Usuario;
import br.com.simplewpps.api.model.Wallpaper;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);
	
	@Query("SELECT u.wppsSalvos FROM Usuario u WHERE u.id = ?1")
	public Page<Wallpaper> getWallpapersSalvos(Long usuarioId, Pageable paginacao);
	
}
