package br.com.simplewpps.api.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="Perfil")
public class TipoPerfil implements GrantedAuthority {

	private static final long serialVersionUID = -3961186406101131306L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
