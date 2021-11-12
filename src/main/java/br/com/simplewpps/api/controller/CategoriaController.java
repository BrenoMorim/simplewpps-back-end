package br.com.simplewpps.api.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.simplewpps.api.model.Categoria;
import br.com.simplewpps.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired 
	private CategoriaRepository catRepository;
	
	@GetMapping
	public Page<CategoriaDto> listarCategorias(@PageableDefault(page = 0, size = 10) Pageable paginacao) {
		Page<Categoria> categorias = catRepository.findAll(paginacao);
		return CategoriaDto.converter(categorias);
	}
	
	@GetMapping("/{nome}")
	public ResponseEntity<CategoriaDto> pesquisarCategoria(@PathVariable String nome) {
		Optional<Categoria> categoria = catRepository.findByNome(nome);
		
		if(categoria.isEmpty()) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(new CategoriaDto(categoria.get()));
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> criarCategoria(@RequestBody @Valid SalvarCategoriaForm form,
													UriComponentsBuilder uriBuilder) {
		if (this.catRepository.findByNome(form.getNome()).isPresent()) {			
			return ResponseEntity.badRequest().body("Já existe uma categoria com esse nome!");
		}
		
		Categoria cat = form.converter();
		this.catRepository.save(cat);
		
		URI uri = uriBuilder.path("/categorias/{nome}").buildAndExpand(cat.getNome()).toUri();
		return ResponseEntity.created(uri).body(new CategoriaDto(cat));
		
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> alterarCategoria(@PathVariable Long id, @RequestBody @Valid SalvarCategoriaForm form,
													UriComponentsBuilder uriBuilder) {
		if (this.catRepository.findByNome(form.getNome()).isPresent()) 			
			return ResponseEntity.badRequest().body("Já existe uma categoria com esse nome!");
		
		
		Optional<Categoria> opt = this.catRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		
		Categoria cat = opt.get();
		cat.setNome(form.getNome());
		this.catRepository.save(cat);
		
		return ResponseEntity.ok(new CategoriaDto(cat));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletarCategoria(@PathVariable Long id) {
		Optional<Categoria> opt = this.catRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		
		Categoria cat = opt.get();
		this.catRepository.delete(cat);
		return ResponseEntity.ok().build();
	}
	
}
