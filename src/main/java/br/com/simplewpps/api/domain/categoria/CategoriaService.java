package br.com.simplewpps.api.domain.categoria;

import java.util.Optional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository catRepository;
	
	public Categoria extrairCategoria(Long id) {
		Optional<Categoria> categoria = catRepository.findById(id);
		if(categoria.isEmpty()) 
			throw new EntityNotFoundException("Categoria não encontrada!");
		return categoria.get();
	}
	
	public Categoria extrairCategoria(String nome) {
		Optional<Categoria> categoria = catRepository.findByNome(nome);
		if(categoria.isEmpty()) 
			throw new EntityNotFoundException("Categoria não encontrada!");
		return categoria.get();
	}
	
	public void verificaSeJaExisteCategoriaComNome(String nome) {
		if (this.catRepository.findByNome(nome).isPresent())	
			throw new EntityExistsException("Já existe uma categoria com esse nome!");
	}
	
	public Page<DadosCategoria> buscarCategorias(Pageable paginacao) {
		Page<Categoria> categorias = catRepository.findAll(paginacao);
		return DadosCategoria.converter(categorias);
	}
	
	public DadosCategoria buscarCategoriaPorNome(String nome) {
		Categoria categoria = this.extrairCategoria(nome);
		return new DadosCategoria(categoria);
	}
	
	@Transactional
	public DadosCategoria salvarCategoria(SalvarCategoriaForm form) {
		Categoria cat = form.converter();
		verificaSeJaExisteCategoriaComNome(cat.getNome());
		this.catRepository.save(cat);
		return new DadosCategoria(cat);
	}
	
	@Transactional
	public DadosCategoria editarCategoria(Long id, SalvarCategoriaForm form) {
		verificaSeJaExisteCategoriaComNome(form.nome());
		
		Categoria categoria = this.extrairCategoria(id);
		categoria.setNome(form.nome());
		this.catRepository.save(categoria);
		
		return new DadosCategoria(categoria);
	}
	
	@Transactional
	public void excluirCategoria(Long id) {
		Categoria cat = this.extrairCategoria(id);
		this.catRepository.delete(cat);
	}
}
