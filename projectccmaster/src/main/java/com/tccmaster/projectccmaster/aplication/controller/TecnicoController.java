package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

    @PostMapping
    @ResponseStatus(CREATED)
    public TecnicoEntity saveTecnico(@RequestBody TecnicoEntity tecnico) {
        try {
            System.out.println("Recebendo técnico: " + tecnico.toString());
            TecnicoEntity saved = tecnicoRepository.save(tecnico);
            System.out.println("Técnico salvo com ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            System.out.println("Erro ao salvar técnico: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("{id}")
    public TecnicoEntity getTecnico(@PathVariable UUID id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Técnico não encontrado"));
    }

    @GetMapping
    public List<TecnicoEntity> listarTecnicos(TecnicoEntity filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<TecnicoEntity> example = Example.of(filtro, matcher);
        return tecnicoRepository.findAll(example);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateTecnico(@PathVariable UUID id, @RequestBody TecnicoEntity tecnico) {
        tecnicoRepository.findById(id)
                .map(tecnicoExistente -> {
                    tecnico.setId(tecnicoExistente.getId());
                    tecnicoRepository.save(tecnico);
                    return tecnicoExistente;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Técnico não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletarTecnico(@PathVariable UUID id){
        tecnicoRepository.findById(id)
                .map(tecnico -> {
                    tecnicoRepository.delete(tecnico);
                    return tecnico;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Técnico não encontrado"));
    }

    // No TecnicoController.java
    @GetMapping("/disponiveis")
    public List<TecnicoEntity> getTecnicosDisponiveis() {
        return tecnicoRepository.findAll();
    }
}