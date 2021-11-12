package br.com.simplewpps.api.config.security;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.simplewpps.api.model.TipoPerfil;
import br.com.simplewpps.api.model.Usuario;
import br.com.simplewpps.api.model.Wallpaper;
import br.com.simplewpps.api.repository.TipoPerfilRepository;
import br.com.simplewpps.api.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

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
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}
	
	public Usuario getUsuario(HttpServletRequest request, UsuarioRepository repository) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		token = token.substring(7, token.length());
		Long idUsuario = this.getIdUsuario(token);
	
		Optional<Usuario> opt = repository.findById(idUsuario);
		Usuario user = opt.isEmpty() ? null : opt.get();
		return user;
	}
	
	public boolean usuarioEhDono(HttpServletRequest request, UsuarioRepository repository, Wallpaper wpp) {
		Usuario user = this.getUsuario(request, repository);
		if (user == null) return false;
		
		if(wpp.getAutor().equals(user)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean usuarioEhModerador(HttpServletRequest request, UsuarioRepository repository, 
			TipoPerfilRepository perfilRepository) {
		Usuario user = this.getUsuario(request, repository);
		TipoPerfil moderador = perfilRepository.findByNome("ROLE_MODERADOR").get();
		if (user == null) return false;
		
		return user.getAuthorities().contains(moderador);
	}
	
}
