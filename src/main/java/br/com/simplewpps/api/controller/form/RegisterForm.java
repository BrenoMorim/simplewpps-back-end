package br.com.simplewpps.api.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.simplewpps.api.model.Usuario;

public class RegisterForm {

	@NotBlank @NotNull @Size(min=3, max=20)
	private String nickname;
	@Email() @NotNull
	private String email;
	@NotBlank @NotNull @Size(min=7, max=30)
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
