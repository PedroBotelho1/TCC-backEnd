// src/main/java/com/tccmaster/projectccmaster/aplication/controller/UsuarioController.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
// ADICIONADO: Import para o PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // ADICIONADO: Injeção do PasswordEncoder para criptografar a senha
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioEntity saveUsuario(@RequestBody UsuarioEntity usuario) {
        // MODIFICADO: A senha agora é criptografada antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @GetMapping("{id}")
    public UsuarioEntity getUsuarioById(@PathVariable UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @GetMapping
    public List<UsuarioEntity> listarUsuarios(UsuarioEntity filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<UsuarioEntity> example = Example.of(filtro, matcher);
        return usuarioRepository.findAll(example);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUsuario(@PathVariable UUID id, @RequestBody UsuarioEntity usuario) {
        usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuario.setId(usuarioExistente.getId());
                    // NOTA: Se permitir atualização de senha, ela também deve ser criptografada aqui.
                    usuarioRepository.save(usuario);
                    return usuarioExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsuario(@PathVariable UUID id) {
        usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return usuario;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}