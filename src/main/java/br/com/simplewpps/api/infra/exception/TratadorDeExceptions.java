package br.com.simplewpps.api.infra.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeExceptions {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarCredenciaisInvalidas(BadCredentialsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity tratarArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity tratarErro401(InsufficientAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity tratarErro400(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAutenticacao(AuthenticationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity tratarErroUsuarioNaoEncontrado(UsernameNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity tratarErroRegistroJaExistente(EntityExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
