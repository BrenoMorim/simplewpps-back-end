package br.com.simplewpps.api.infra.security;

import java.util.Date;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.simplewpps.api.domain.perfil.TipoPerfil;
import br.com.simplewpps.api.domain.usuario.Usuario;
import br.com.simplewpps.api.domain.wallpaper.Wallpaper;
import br.com.simplewpps.api.domain.perfil.TipoPerfilRepository;
import br.com.simplewpps.api.domain.usuario.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${api.jwt.expiration}")
	private String expiration;
	@Value("${api.jwt.secret}")
	private String secret;
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private TipoPerfilRepository perfilRepository;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API do Fórum da Alura")
				.setSubject(logado.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
			return Long.parseLong(claims.getSubject());
		} catch (IllegalArgumentException ex) {
			throw new InsufficientAuthenticationException(ex.getMessage());
		}
	}
	
	public Usuario getUsuario(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		token = token.substring(7, token.length());
		Long idUsuario = this.getIdUsuario(token);
	
		Optional<Usuario> opt = this.repository.findById(idUsuario);
		Usuario user = opt.isEmpty() ? null : opt.get();
		return user;
	}
	
	public boolean usuarioEhDono(HttpServletRequest request, Wallpaper wpp) {
		Usuario user = this.getUsuario(request);
		if (user == null) return false;
		
		return wpp.getAutor().equals(user);
	}
	
	public boolean usuarioEhModerador(HttpServletRequest request) {
		Usuario user = this.getUsuario(request);
		TipoPerfil moderador = this.perfilRepository.findByNome("ROLE_MODERADOR").get();
		if (user == null) return false;
		
		return user.getAuthorities().contains(moderador);
	}
	
}
