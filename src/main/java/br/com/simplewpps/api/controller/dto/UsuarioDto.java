package br.com.simplewpps.api.controller.dto;

public class UsuarioDto {
	
	private String nickname;
	private String email;
	
	public UsuarioDto(String nickname, String email) {
		this.email = email;
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}
	public String getEmail() {
		return email;
	}

}
