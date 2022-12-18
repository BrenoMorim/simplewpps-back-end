package br.com.simplewpps.api.domain.categoria;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Categoria")
@Table(name = "categorias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "nome"})
public class Categoria {
	@Id
	private Long id = Long.valueOf(LocalDateTime.now().hashCode());
	private String nome;
	
}
