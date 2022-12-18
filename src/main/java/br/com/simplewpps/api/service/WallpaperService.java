package br.com.simplewpps.api.service;

import java.util.HashSet;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import br.com.simplewpps.api.controller.dto.DetailedWallpaperDto;
import br.com.simplewpps.api.controller.dto.WallpaperDto;
import br.com.simplewpps.api.controller.form.SalvarWallpaperForm;
import br.com.simplewpps.api.model.Categoria;
import br.com.simplewpps.api.model.Usuario;
import br.com.simplewpps.api.model.Wallpaper;
import br.com.simplewpps.api.repository.CategoriaRepository;
import br.com.simplewpps.api.repository.UsuarioRepository;
import br.com.simplewpps.api.repository.WallpaperRepository;

@Service
public class WallpaperService {
	
	@Autowired
	private WallpaperRepository wppRepository;
	@Autowired
	private CategoriaRepository catRepository;
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private TokenService tokenService;
	
	public Wallpaper extrairWallpaper(Long id) {
		Optional<Wallpaper> opt = this.wppRepository.findById(id);
		if (opt.isEmpty())
			throw new EntityNotFoundException("Wallpaper não encontrado");
		return opt.get();
	}
	
	public HashSet<Categoria> extrairCategorias(SalvarWallpaperForm form) {
		HashSet<Categoria> categorias = form.getCategoriasBanco(catRepository);
		if (categorias == null) 
			throw new IllegalArgumentException("Categorias inválidas, um wallpaper deve ter entre uma e cinco categorias");
		return categorias;
	}
	
	public Usuario extrairUsuario(HttpServletRequest request) {
		Usuario user = this.tokenService.getUsuario(request);
		if (user == null) throw new InsufficientAuthenticationException("Usuário nulo!");
		return user;
	}
	
	public void verificaSeTemPermissao(HttpServletRequest request, Wallpaper wpp) {
		if (!this.tokenService.usuarioEhDono(request, wpp) &&
				!this.tokenService.usuarioEhModerador(request))
			throw new BadCredentialsException("Acesso negado!");
	}
	
	public Page<WallpaperDto> buscarWallpapers(String titulo, String categoriaNome, Pageable paginacao) {
		Page<Wallpaper> wpps;
		if (titulo == null && categoriaNome == null) {
			wpps = wppRepository.findAll(paginacao);
		} else if (titulo != null && categoriaNome == null) {
			wpps = wppRepository.findByTitulo(titulo, paginacao);
		} else if (titulo == null && categoriaNome != null) {
			wpps = wppRepository.findByCategoriasNome(categoriaNome, paginacao);
		} else {
			wpps = wppRepository.findByTituloAndCategoriasNome(titulo, categoriaNome, paginacao);
		}
		return WallpaperDto.converter(wpps);
	}
	
	public DetailedWallpaperDto buscarWallpaperPorId(Long id) {
		Wallpaper wpp = this.extrairWallpaper(id);
		return new DetailedWallpaperDto(wpp);
	}
	
	@Transactional
	public WallpaperDto criarWallpaper(SalvarWallpaperForm form, HttpServletRequest request) {
		HashSet<Categoria> categorias = this.extrairCategorias(form);
		
		Wallpaper wpp = form.converter();
		categorias.forEach(cat -> wpp.adicionarCategoria(cat));
		
		Usuario user = this.extrairUsuario(request);
		wpp.setAutor(user);
		this.wppRepository.save(wpp);
		
		return new WallpaperDto(wpp);
	}
	
	@Transactional
	public WallpaperDto editarWallpaper(Long id, SalvarWallpaperForm form, HttpServletRequest request) {	
		Wallpaper wpp = this.extrairWallpaper(id);
		
		this.verificaSeTemPermissao(request, wpp);
		
		wpp.setTitulo(form.getTitulo());
		wpp.setUrl(form.getUrl());
		wpp.resetarCategorias();
	
		HashSet<Categoria> categorias = extrairCategorias(form);
		categorias.forEach(cat -> wpp.adicionarCategoria(cat));
		this.wppRepository.save(wpp);
		return new WallpaperDto(wpp);
	}
	
	@Transactional
	public void excluirWallpaper(Long id, HttpServletRequest request) {
		Wallpaper wpp = this.extrairWallpaper(id);
		this.verificaSeTemPermissao(request, wpp);
		this.wppRepository.delete(wpp);
	}
	
	@Transactional
	public void curtirWallpaper(Long id, HttpServletRequest request) {
		Wallpaper wpp = this.extrairWallpaper(id);
		Usuario user = this.extrairUsuario(request);
		user.curtirWallpaper(wpp);
	}
	
	@Transactional
	public void descurtirWallpaper(Long id, HttpServletRequest request) {
		Wallpaper wpp = this.extrairWallpaper(id);
		Usuario user = this.extrairUsuario(request);
		user.descurtirWallpaper(wpp);
	}
	
	public Page<WallpaperDto> buscarWallpapersSalvos(HttpServletRequest request, Pageable paginacao) {
		Usuario user = this.extrairUsuario(request);
		Page<Wallpaper> wpps = userRepository.getWallpapersSalvos(user.getId(), paginacao);
		return WallpaperDto.converter(wpps);
	}
}
