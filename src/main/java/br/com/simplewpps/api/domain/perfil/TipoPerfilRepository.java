package br.com.simplewpps.api.domain.perfil;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPerfilRepository extends JpaRepository<TipoPerfil, Long> {
	
	public Optional<TipoPerfil> findByNome(String nome);
	
}
