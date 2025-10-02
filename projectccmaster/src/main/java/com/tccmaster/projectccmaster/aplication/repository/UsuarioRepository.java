package com.tccmaster.projectccmaster.aplication.repository;

import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // Adicionar este import
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    // Adicione este método
    Optional<UsuarioEntity> findByEmail(String email);
}