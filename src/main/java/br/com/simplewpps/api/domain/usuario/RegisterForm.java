package br.com.simplewpps.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {

	@NotBlank @Size(min=3, max=20)
	private String nickname;
	@Email() @NotNull
	private String email;
	@NotBlank @Size(min=7, max=30)
	private String senha;
	public Usuario converter() {
		return new Usuario(this.nickname, this.email, this.senha);
	}
}
