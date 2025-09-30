// Project_TccMaster-main/projectccmaster/src/main/java/com/tccmaster/projectccmaster/aplication/repository/FeedbackRepository.java
package com.tccmaster.projectccmaster.aplication.repository;


import com.tccmaster.projectccmaster.aplication.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {

    List<FeedbackEntity> findByUsuarioId(UUID usuarioId);

    @Query("SELECT f FROM FeedbackEntity f") // É uma boa prática usar o nome da Entidade no HQL
    List<FeedbackEntity> buscarTodosOsFeedbacks();
}