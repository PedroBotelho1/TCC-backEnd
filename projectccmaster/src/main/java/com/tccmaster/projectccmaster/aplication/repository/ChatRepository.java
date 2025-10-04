// Em: src/main/java/com/tccmaster/projectccmaster/aplication/repository/ChatRepository.java
package com.tccmaster.projectccmaster.aplication.repository;

import com.tccmaster.projectccmaster.aplication.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByRoomIdOrderByTimestamp(String roomId);
    void deleteByRoomId(String roomId);

    // --- NOVO MÉTODO ADICIONADO ---
    // Deleta todas as mensagens cujo roomId contenha o ID do participante (usuário ou técnico).
    void deleteByRoomIdContaining(String participantId);
}