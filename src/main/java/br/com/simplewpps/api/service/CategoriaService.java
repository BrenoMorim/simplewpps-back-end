package br.com.simplewpps.api.service;

import java.util.Optional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.simplewpps.api.controller.dto.CategoriaDto;
import br.com.simplewpps.api.controller.form.SalvarCategoriaForm;
import br.com.simplewpps.api.model.Categoria;
import br.com.simplewpps.api.repository.CategoriaRepository;

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
	
	public Page<CategoriaDto> buscarCategorias(Pageable paginacao) {		
		Page<Categoria> categorias = catRepository.findAll(paginacao);
		return CategoriaDto.converter(categorias);
	}
	
	public CategoriaDto buscarCategoriaPorNome(String nome) {
		Categoria categoria = this.extrairCategoria(nome);
		return new CategoriaDto(categoria);
	}
	
	@Transactional
	public CategoriaDto salvarCategoria(SalvarCategoriaForm form) {
		Categoria cat = form.converter();
		verificaSeJaExisteCategoriaComNome(cat.getNome());
		this.catRepository.save(cat);
		return new CategoriaDto(cat);
	}
	
	@Transactional
	public CategoriaDto editarCategoria(Long id, SalvarCategoriaForm form) {
		verificaSeJaExisteCategoriaComNome(form.getNome());
		
		Categoria categoria = this.extrairCategoria(id);
		categoria.setNome(form.getNome());
		this.catRepository.save(categoria);
		
		return new CategoriaDto(categoria);
	}
	
	@Transactional
	public void excluirCategoria(Long id) {
		Categoria cat = this.extrairCategoria(id);
		this.catRepository.delete(cat);
	}
}
