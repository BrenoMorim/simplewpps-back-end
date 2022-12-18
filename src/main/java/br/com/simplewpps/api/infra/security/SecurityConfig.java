package br.com.simplewpps.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.simplewpps.api.domain.usuario.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	AutenticacaoViaTokenFilter securityFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeHttpRequests()
				.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
				.requestMatchers(HttpMethod.GET, "/wpps").permitAll()
				.requestMatchers(HttpMethod.GET, "/wpps/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/categorias").permitAll()
				.requestMatchers(HttpMethod.GET, "/categorias/*").permitAll()
				.requestMatchers("/categorias").hasRole("ROLE_MODERADOR")
				.requestMatchers("/categorias/*").hasRole("ROLE_MODERADOR")
				.anyRequest().authenticated()
				.and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
