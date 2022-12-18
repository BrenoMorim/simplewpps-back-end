package br.com.simplewpps.api.domain.perfil;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "Perfil")
@Table(name="perfis")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "nome"})
public class TipoPerfil implements GrantedAuthority {
	@Id
	private Long id = Long.valueOf(LocalDateTime.now().hashCode());
	private String nome;

	@Override
	public String getAuthority() {
		return this.nome;
	}

}
