package br.com.simplewpps.api.domain.usuario;

import br.com.simplewpps.api.domain.wallpaper.Wallpaper;
import br.com.simplewpps.api.domain.perfil.TipoPerfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {
	@Id
	private Long id = Long.valueOf(LocalDateTime.now().hashCode());
	private String nickname;
	private String email;
	private String senha;
	private LocalDateTime dataCadastro = LocalDateTime.now();

	public Usuario(String nickname, String email, String senha) {
		this.nickname = nickname;
		this.email = email;
		this.senha = senha;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<TipoPerfil> perfis = new HashSet<TipoPerfil>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Wallpaper> wppsSalvos = new HashSet<Wallpaper>();

	public void adicionaPerfil(TipoPerfil perfil) {
		this.perfis.add(perfil);
	}
	
	public Set<Wallpaper> getWppsSalvos() {
		return Set.copyOf(this.wppsSalvos);
	}
	
	public void curtirWallpaper(Wallpaper wpp) {
		this.wppsSalvos.add(wpp);
	}
	
	public void descurtirWallpaper(Wallpaper wpp) {
		this.wppsSalvos.remove(wpp);
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
