// Em: src/main/java/com/tccmaster/projectccmaster/aplication/controller/TecnicoController.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.repository.ChatRepository; // <-- IMPORTADO
import com.tccmaster.projectccmaster.aplication.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTADO
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- NOVA INJEÇÃO ---
    @Autowired
    private ChatRepository chatRepository;


    @PostMapping
    @ResponseStatus(CREATED)
    public TecnicoEntity saveTecnico(@RequestBody TecnicoEntity tecnico) {
        String senhaCriptografada = passwordEncoder.encode(tecnico.getSenha());
        tecnico.setSenha(senhaCriptografada);
        return tecnicoRepository.save(tecnico);
    }

    // ... outros métodos (get, put, etc) ...

    @GetMapping("{id}")
    public TecnicoEntity getTecnico(@PathVariable UUID id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Técnico não encontrado"));
    }

    @GetMapping
    public List<TecnicoEntity> listarTecnicos(TecnicoEntity filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<TecnicoEntity> example = Example.of(filtro, matcher);
        return tecnicoRepository.findAll(example);
    }

    @GetMapping("/disponiveis")
    public List<TecnicoEntity> getTecnicosDisponiveis() {
        return tecnicoRepository.findByAtivoTrue();
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateTecnico(@PathVariable UUID id, @RequestBody TecnicoEntity tecnico) {
        tecnicoRepository.findById(id)
                .map(tecnicoExistente -> {
                    tecnico.setId(tecnicoExistente.getId());
                    if (tecnico.getSenha() != null && !tecnico.getSenha().isEmpty()) {
                        tecnico.setSenha(passwordEncoder.encode(tecnico.getSenha()));
                    } else {
                        tecnico.setSenha(tecnicoExistente.getSenha());
                    }
                    tecnicoRepository.save(tecnico);
                    return tecnicoExistente;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Técnico não encontrado"));
    }

    // --- MÉTODO ATUALIZADO PARA EXCLUSÃO REAL E EM CASCATA ---
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Transactional // Garante que ambas as operações (deletar chat e técnico) ocorram com sucesso
    public void deletarTecnico(@PathVariable UUID id){
        tecnicoRepository.findById(id)
                .map(tecnico -> {
                    // 1. Deleta o histórico de chats associado
                    chatRepository.deleteByRoomIdContaining(id.toString());

                    // 2. Deleta o próprio técnico (feedbacks serão deletados em cascata)
                    tecnicoRepository.delete(tecnico);
                    return tecnico;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Técnico não encontrado"));
    }
}