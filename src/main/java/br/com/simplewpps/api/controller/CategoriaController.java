package br.com.simplewpps.api.controller;

import java.net.URI;

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

import br.com.simplewpps.api.domain.categoria.DadosCategoria;
import br.com.simplewpps.api.domain.categoria.SalvarCategoriaForm;
import br.com.simplewpps.api.domain.categoria.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService service;
	
	@GetMapping
	@Cacheable(value = "listaDeCategorias")
	public Page<DadosCategoria> listarCategorias(@PageableDefault(page = 0, size = 10) Pageable paginacao) {
		return service.buscarCategorias(paginacao);
	}
	
	@GetMapping("/{nome}")
	public ResponseEntity<DadosCategoria> pesquisarCategoria(@PathVariable String nome) {
		DadosCategoria categoria = service.buscarCategoriaPorNome(nome);
		return ResponseEntity.ok(categoria);
	}
	
	@PostMapping
	@CacheEvict(value = "listaDeCategorias", allEntries = true)
	public ResponseEntity<?> criarCategoria(@RequestBody @Valid SalvarCategoriaForm form, UriComponentsBuilder uriBuilder) {

		DadosCategoria dto = service.salvarCategoria(form);

		URI uri = uriBuilder.path("/categorias/{nome}").buildAndExpand(dto.getNome()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping("/{id}")
	@CacheEvict(value = "listaDeCategorias", allEntries = true)
	public ResponseEntity<?> alterarCategoria(@PathVariable Long id, @RequestBody @Valid SalvarCategoriaForm form) {
		DadosCategoria dto = service.editarCategoria(id, form);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping("/{id}")
	@CacheEvict(value = "listaDeCategorias", allEntries = true)
	public ResponseEntity<?> deletarCategoria(@PathVariable Long id) {
		service.excluirCategoria(id);
		return ResponseEntity.noContent().build();
	}
	
}
