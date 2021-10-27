package br.com.simplewpps.api.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 7827011222191801266L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nickname;
	private String email;
	private String senha;
	private LocalDateTime dataCadastro = LocalDateTime.now();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<TipoPerfil> perfis = new HashSet<TipoPerfil>();
	
	@Override
	public int hashCode() {
		return Objects.hash(email, id, nickname, perfis, senha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(nickname, other.nickname) && Objects.equals(perfis, other.perfis)
				&& Objects.equals(senha, other.senha);
	}

	public Usuario() {
		
	}
	
	public Usuario(String nickname, String email, String senha) {
		this.nickname = nickname;
		this.email = email;
		this.senha = senha;
	}
	
	public void adicionaPerfil(TipoPerfil perfil) {
		this.perfis.add(perfil);
	}
	
	public Long getId() {
		return id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public LocalDateTime getDataCadastro() {
		return dataCadastro;
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
