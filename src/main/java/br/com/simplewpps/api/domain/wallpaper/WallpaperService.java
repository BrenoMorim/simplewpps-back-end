package br.com.simplewpps.api.domain.wallpaper;

import java.util.HashSet;
import java.util.Optional;

import br.com.simplewpps.api.infra.security.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import br.com.simplewpps.api.domain.categoria.Categoria;
import br.com.simplewpps.api.domain.usuario.Usuario;
import br.com.simplewpps.api.domain.categoria.CategoriaRepository;
import br.com.simplewpps.api.domain.usuario.UsuarioRepository;

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
		Wallpaper wpp = this.wppRepository.getReferenceById(id);
		return wpp;
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
			throw new InsufficientAuthenticationException("Acesso negado!");
	}
	
	public Page<DadosWallpaper> buscarWallpapers(String titulo, String categoriaNome, Pageable paginacao) {
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
		return DadosWallpaper.converter(wpps);
	}
	
	public DadosDetalhadosWallpaper buscarWallpaperPorId(Long id) {
		Wallpaper wpp = this.extrairWallpaper(id);
		return new DadosDetalhadosWallpaper(wpp);
	}
	
	@Transactional
	public DadosWallpaper criarWallpaper(SalvarWallpaperForm form, HttpServletRequest request) {
		HashSet<Categoria> categorias = this.extrairCategorias(form);
		
		Wallpaper wpp = form.converter();
		categorias.forEach(cat -> wpp.adicionarCategoria(cat));
		
		Usuario user = this.extrairUsuario(request);
		wpp.setAutor(user);
		this.wppRepository.save(wpp);
		
		return new DadosWallpaper(wpp);
	}
	
	@Transactional
	public DadosWallpaper editarWallpaper(Long id, SalvarWallpaperForm form, HttpServletRequest request) {
		Wallpaper wpp = this.extrairWallpaper(id);
		
		this.verificaSeTemPermissao(request, wpp);

		if (form.getUrl() != null) {
			wpp.setUrl(form.getUrl());
		}
		if (form.getTitulo() != null) {
			wpp.setTitulo(form.getTitulo());
		}
		if (form.getCategorias() != null) {
			HashSet<Categoria> categorias = extrairCategorias(form);
			if (categorias.size() > 0) {
				wpp.resetarCategorias();
				categorias.forEach(cat -> wpp.adicionarCategoria(cat));
			}
		}
		return new DadosWallpaper(wpp);
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
	
	public Page<DadosWallpaper> buscarWallpapersSalvos(HttpServletRequest request, Pageable paginacao) {
		Usuario user = this.extrairUsuario(request);
		Page<Wallpaper> wpps = userRepository.getWallpapersSalvos(user.getId(), paginacao);
		return DadosWallpaper.converter(wpps);
	}
}
