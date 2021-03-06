package br.com.simplewpps.api.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.simplewpps.api.model.Categoria;

@Repository
public interface CategoriaRepository extends PagingAndSortingRepository<Categoria, Long> {
	
	public Optional<Categoria> findByNome(String nome);
	
}
