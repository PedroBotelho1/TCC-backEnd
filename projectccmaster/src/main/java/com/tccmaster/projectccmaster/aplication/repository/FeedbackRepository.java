// Em: src/main/java/com/tccmaster/projectccmaster/aplication/repository/FeedbackRepository.java
package com.tccmaster.projectccmaster.aplication.repository;

import com.tccmaster.projectccmaster.aplication.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {

    List<FeedbackEntity> findByUsuarioId(UUID usuarioId);

    // --- NOVO MÉTODO ADICIONADO ---
    // Busca todos os feedbacks associados a um ID de técnico específico.
    List<FeedbackEntity> findByTecnicoId(UUID tecnicoId);

    List<FeedbackEntity> findByAutorTecnicoId(UUID autorTecnicoId);

    @Query("SELECT f FROM FeedbackEntity f")
    List<FeedbackEntity> buscarTodosOsFeedbacks();
}