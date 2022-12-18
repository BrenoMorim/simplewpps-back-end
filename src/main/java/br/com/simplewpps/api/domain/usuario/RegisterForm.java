package br.com.simplewpps.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterForm {

	@NotBlank @Size(min=3, max=20)
	private String nickname;
	@Email() @NotNull
	private String email;
	@NotBlank @Size(min=7, max=30)
	private String senha;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Usuario converter() {
		return new Usuario(this.nickname, this.email, this.senha);
	}
}
