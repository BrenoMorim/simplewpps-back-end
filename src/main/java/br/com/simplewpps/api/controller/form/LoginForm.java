package br.com.simplewpps.api.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginForm {
	@NotNull @NotBlank @Email
	private String email;
	@NotNull @NotBlank @Size(min=7, max=30)
	private String senha;

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, senha);
	}
}
