package br.com.simplewpps.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.simplewpps.api.model.TipoPerfil;

public interface TipoPerfilRepository extends JpaRepository<TipoPerfil, Long> {
	
	public Optional<TipoPerfil> findByNome(String nome);
	
}
