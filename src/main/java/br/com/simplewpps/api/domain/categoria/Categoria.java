package br.com.simplewpps.api.domain.categoria;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

@Entity(name = "Categoria")
@Table(name = "categorias")
public class Categoria {
	
	@Id
	private Long id = Long.valueOf(LocalDateTime.now().hashCode());
	private String nome;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	
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
		Categoria other = (Categoria) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}
	
}
