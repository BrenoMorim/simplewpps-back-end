package br.com.simplewpps.api.domain.perfil;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity(name = "Perfil")
@Table(name="perfis")
public class TipoPerfil implements GrantedAuthority {

	private static final long serialVersionUID = -3961186406101131306L;

	@Id
	private Long id = Long.valueOf(LocalDateTime.now().hashCode());
	private String nome;
	
	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoPerfil other = (TipoPerfil) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}

	public Long getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String getAuthority() {
		return this.nome;
	}

	
	
}
