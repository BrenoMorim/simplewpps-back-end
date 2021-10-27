package br.com.simplewpps.api.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import br.com.simplewpps.api.config.security.TokenService;
import br.com.simplewpps.api.controller.dto.DetailedWallpaperDto;
import br.com.simplewpps.api.controller.dto.WallpaperDto;
import br.com.simplewpps.api.controller.form.SalvarWallpaperForm;
import br.com.simplewpps.api.model.Categoria;
import br.com.simplewpps.api.model.TipoPerfil;
import br.com.simplewpps.api.model.Usuario;
import br.com.simplewpps.api.model.Wallpaper;
import br.com.simplewpps.api.repository.CategoriaRepository;
import br.com.simplewpps.api.repository.TipoPerfilRepository;
import br.com.simplewpps.api.repository.UsuarioRepository;
import br.com.simplewpps.api.repository.WallpaperRepository;

@RestController
@RequestMapping("/wpps")
public class WallpaperController {
	
	@Autowired
	private TokenService tokenService;
	@Autowired 
	private CategoriaRepository catRepository;
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private WallpaperRepository wppRepository;
	@Autowired 
	private TipoPerfilRepository perfilRepository;
	
	@GetMapping
	public Page<WallpaperDto> listarWpps(@RequestParam(required = false) String titulo, 
			@PageableDefault(sort = "dataCriacao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		
		if (titulo == null) {
			Page<Wallpaper> wpps = wppRepository.findAll(paginacao);
			return WallpaperDto.converter(wpps);
		} else {
			Page<Wallpaper> wpps = wppRepository.findByTitulo(titulo, paginacao);
			return WallpaperDto.converter(wpps);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetailedWallpaperDto> detalharWalpaper(@PathVariable Long id) {
		Optional<Wallpaper> opt = this.wppRepository.findById(id);
		if (opt.isPresent()) {
			return ResponseEntity.ok(new DetailedWallpaperDto(opt.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping()
	@Transactional
	public ResponseEntity<?> salvarWallpaper(@Valid @RequestBody SalvarWallpaperForm form, HttpServletRequest request, UriComponentsBuilder uriBuilder) {
		
		HashSet<Categoria> categorias = form.getCategoriasBanco(catRepository);
		if (categorias == null) return ResponseEntity.badRequest().body("Categorias inválidas, um wallpaper deve ter entre uma e cinco categorias");
		
		Wallpaper wpp = form.converter();
		categorias.forEach(cat -> wpp.adicionarCategoria(cat));
		
		Usuario user = this.tokenService.getUsuario(request, this.userRepository);
		if (user == null) return ResponseEntity.badRequest().body("Usuário nulo!");
		wpp.setAutor(user);
		this.wppRepository.save(wpp);
		
		URI uri = uriBuilder.path("/wpps/{id}").buildAndExpand(wpp.getId()).toUri();
		return ResponseEntity.created(uri).body(new WallpaperDto(wpp));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> editarWallpaper(@PathVariable Long id, @Valid @RequestBody SalvarWallpaperForm form, HttpServletRequest request) {
		Optional<Wallpaper> opt = this.wppRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();		
		Wallpaper wpp = opt.get();
		TipoPerfil moderador = this.perfilRepository.findByNome("ROLE_MODERADOR").get();
		
		if (!this.tokenService.usuarioEhDono(request, userRepository, wpp) ||
				!this.tokenService.getUsuario(request, userRepository).getAuthorities().contains(moderador)) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		
		wpp.setDescricao(form.getDescricao());
		wpp.setTitulo(form.getTitulo());
		wpp.setUrl(form.getUrl());
		wpp.resetarCategorias();
	
		HashSet<Categoria> categorias = form.getCategoriasBanco(catRepository);
		if (categorias == null) return ResponseEntity.badRequest().body("Categorias inválidas, um wallpaper deve ter entre uma e cinco categorias");
		
		categorias.forEach(cat -> wpp.adicionarCategoria(cat));
		this.wppRepository.save(wpp);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletarWpp(@PathVariable Long id, HttpServletRequest request) {
		TipoPerfil moderador = this.perfilRepository.findByNome("ROLE_MODERADOR").get();
		
		Optional<Wallpaper> opt = this.wppRepository.findById(id);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		Wallpaper wpp = opt.get();
		
		if (!this.tokenService.usuarioEhDono(request, userRepository, wpp) || 
				!this.tokenService.getUsuario(request, userRepository).getAuthorities().contains(moderador)) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			
		this.wppRepository.delete(wpp);
		return ResponseEntity.ok().build();
	}
}
