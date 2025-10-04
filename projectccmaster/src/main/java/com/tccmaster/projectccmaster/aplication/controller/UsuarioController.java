// Em: src/main/java/com/tccmaster/projectccmaster/aplication/controller/UsuarioController.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.repository.ChatRepository; // <-- IMPORTADO
import com.tccmaster.projectccmaster.aplication.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTADO
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- NOVA INJEÇÃO ---
    @Autowired
    private ChatRepository chatRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioEntity saveUsuario(@RequestBody UsuarioEntity usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // ... outros métodos (get, put, etc.) ...

    @GetMapping("{id}")
    public UsuarioEntity getUsuarioById(@PathVariable UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @GetMapping
    public List<UsuarioEntity> listarUsuarios(UsuarioEntity filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<UsuarioEntity> example = Example.of(filtro, matcher);
        return usuarioRepository.findAll(example);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUsuario(@PathVariable UUID id, @RequestBody UsuarioEntity usuario) {
        usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuario.setId(usuarioExistente.getId());
                    usuarioRepository.save(usuario);
                    return usuarioExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }


    // --- MÉTODO ATUALIZADO ---
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional // Garante que ambas as operações (deletar chat e usuário) ocorram com sucesso
    public void deleteUsuario(@PathVariable UUID id) {
        usuarioRepository.findById(id)
                .map(usuario -> {
                    // 1. Deleta o histórico de chats associado
                    chatRepository.deleteByRoomIdContaining(id.toString());

                    // 2. Deleta o usuário (feedbacks serão deletados em cascata)
                    usuarioRepository.delete(usuario);
                    return usuario;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}