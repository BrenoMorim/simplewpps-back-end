package br.com.simplewpps.api.controller;

import java.net.URI;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.simplewpps.api.controller.dto.CategoriaDto;
import br.com.simplewpps.api.controller.form.SalvarCategoriaForm;
import br.com.simplewpps.api.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService service;
	
	@GetMapping
	@Cacheable(value = "listaDeCategorias")
	public Page<CategoriaDto> listarCategorias(@PageableDefault(page = 0, size = 10) Pageable paginacao) {
		return service.buscarCategorias(paginacao);
	}
	
	@GetMapping("/{nome}")
	public ResponseEntity<CategoriaDto> pesquisarCategoria(@PathVariable String nome) {
		try {			
			CategoriaDto categoria = service.buscarCategoriaPorNome(nome);
			return ResponseEntity.ok(categoria);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	@CacheEvict(value = "listaDeCategorias", allEntries = true)
	public ResponseEntity<?> criarCategoria(@RequestBody @Valid SalvarCategoriaForm form, UriComponentsBuilder uriBuilder) {
		try {
			
			CategoriaDto dto = service.salvarCategoria(form);
			
			URI uri = uriBuilder.path("/categorias/{nome}").buildAndExpand(dto.getNome()).toUri();
			return ResponseEntity.created(uri).body(dto);
		} catch (EntityExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("/{id}")
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> alterarCategoria(@PathVariable Long id, @RequestBody @Valid SalvarCategoriaForm form) {
		try {
			CategoriaDto dto = service.editarCategoria(id, form);
			return ResponseEntity.ok(dto);
		} catch(EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (EntityExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@DeleteMapping("/{id}")
	@CacheEvict(value = "listaDeCategorias", allEntries = true)
	public ResponseEntity<?> deletarCategoria(@PathVariable Long id) {
		try {
			service.excluirCategoria(id);
			return ResponseEntity.noContent().build();
		} catch(EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
