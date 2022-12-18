package br.com.simplewpps.api.controller;

import java.net.URI;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.simplewpps.api.domain.wallpaper.DadosDetalhadosWallpaper;
import br.com.simplewpps.api.domain.wallpaper.DadosWallpaper;
import br.com.simplewpps.api.domain.wallpaper.SalvarWallpaperForm;
import br.com.simplewpps.api.domain.wallpaper.WallpaperService;

@RestController
@RequestMapping("/wpps")
public class WallpaperController {
	
	@Autowired
	private WallpaperService service;
	
	@GetMapping
	public Page<DadosWallpaper> listarWpps(@RequestParam(required = false) String titulo,
                                           @RequestParam(required = false) String categoriaNome,
                                           @PageableDefault(sort = "dataCriacao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		
		return service.buscarWallpapers(titulo, categoriaNome, paginacao);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DadosDetalhadosWallpaper> detalharWallpaper(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(service.buscarWallpaperPorId(id));
		} catch(EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PostMapping()
	public ResponseEntity<?> salvarWallpaper(@Valid @RequestBody SalvarWallpaperForm form, HttpServletRequest request, UriComponentsBuilder uriBuilder) {
		try {
			DadosWallpaper dto = service.criarWallpaper(form, request);
			URI uri = uriBuilder.path("/wpps/{id}").buildAndExpand(dto.id()).toUri();
			return ResponseEntity.created(uri).body(dto);
		} catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editarWallpaper(@PathVariable Long id, @RequestBody SalvarWallpaperForm form, HttpServletRequest request) {
		try {
			this.service.editarWallpaper(id, form, request);
			return ResponseEntity.ok().build();
		} catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarWpp(@PathVariable Long id, HttpServletRequest request) {
		try {
			this.service.excluirWallpaper(id, request);
			return ResponseEntity.ok().build();
		} catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch(EntityNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/curtir/{id}")
	@Transactional
	public ResponseEntity<?> curtirWallpaper(@PathVariable Long id, HttpServletRequest request) {
		try {
			this.service.curtirWallpaper(id, request);
			return ResponseEntity.ok().build();
		} catch(InsufficientAuthenticationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch(EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/descurtir/{id}")
	@Transactional
	public ResponseEntity<?> descurtirWallpaper(@PathVariable Long id, HttpServletRequest request) {
		try {
			this.service.descurtirWallpaper(id, request);
			return ResponseEntity.ok().build();
		} catch(InsufficientAuthenticationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch(EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@GetMapping("/salvos")
	public Page<DadosWallpaper> listarWallpapersSalvos(@PageableDefault(direction = Direction.DESC, page = 0, size = 10) Pageable paginacao, HttpServletRequest request) {
		try {			
			return this.service.buscarWallpapersSalvos(request, paginacao);
		} catch (Exception e) {
			return null;
		}
	}
}
