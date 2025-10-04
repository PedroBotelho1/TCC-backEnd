// Em: src/main/java/com/tccmaster/projectccmaster/aplication/repository/TecnicoRepository.java
package com.tccmaster.projectccmaster.aplication.repository;

import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // ADICIONE ESTE IMPORT
import java.util.UUID;
import java.util.List; // Import já deve existir

public interface TecnicoRepository extends JpaRepository<TecnicoEntity, UUID> {
    List<TecnicoEntity> findByAtivoTrue();

    // --- NOVO MÉTODO ---
    // Adicione este método para permitir a busca de técnico por email
    Optional<TecnicoEntity> findByEmail(String email);
}