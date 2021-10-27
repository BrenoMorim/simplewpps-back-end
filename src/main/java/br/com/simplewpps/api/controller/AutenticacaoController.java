package br.com.simplewpps.api.controller;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.simplewpps.api.config.security.TokenService;
import br.com.simplewpps.api.controller.dto.TokenDto;
import br.com.simplewpps.api.controller.dto.UsuarioDto;
import br.com.simplewpps.api.controller.form.LoginForm;
import br.com.simplewpps.api.controller.form.RegisterForm;
import br.com.simplewpps.api.model.TipoPerfil;
import br.com.simplewpps.api.model.Usuario;
import br.com.simplewpps.api.repository.TipoPerfilRepository;
import br.com.simplewpps.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private TipoPerfilRepository perfilRepository;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/register")
	@Transactional
	public ResponseEntity<?> registrar(@RequestBody @Valid RegisterForm form) {
		
		Optional<Usuario> verificarUsuario = usuarioRepository.findByEmail(form.getEmail());
		if(verificarUsuario.isEmpty()) {
			
			Usuario usuario = form.converter();
			String senhaBruta = usuario.getSenha();
			usuario.setSenha(new BCryptPasswordEncoder().encode(senhaBruta));
			TipoPerfil perfil = perfilRepository.findByNome("ROLE_USUARIO").get();
			
			usuario.adicionaPerfil(perfil);
			this.usuarioRepository.save(usuario);
			return ResponseEntity.ok(new UsuarioDto(usuario.getNickname(), usuario.getEmail()));
			
		} else {
			return ResponseEntity.badRequest().body("Já existe um usuário com este email!");
		}
	}
	
}
