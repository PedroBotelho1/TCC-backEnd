// Em: src/main/java/com/tccmaster/projectccmaster/aplication/Service/ChatManagementService.java
package com.tccmaster.projectccmaster.aplication.Service;

import com.tccmaster.projectccmaster.aplication.entity.ChatMessageEntity;
import com.tccmaster.projectccmaster.aplication.repository.ChatRepository;
import com.tccmaster.projectccmaster.aplication.webSocket.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatManagementService {

    @Autowired
    private ChatRepository chatRepository;

    // ESTE MÉTODO AGORA BUSCA OS CHAMADOS DIRETAMENTE DO BANCO
    public List<Map<String, String>> getConversasParaTecnico(String tecnicoId) {
        // Encontra todas as mensagens salvas
        List<ChatMessageEntity> todasAsMensagens = chatRepository.findAll();

        // Agrupa as mensagens por 'roomId' e depois formata para a lista de chamados
        return todasAsMensagens.stream()
                // Filtra apenas as conversas que pertencem a este técnico
                .filter(msg -> msg.getRoomId() != null && msg.getRoomId().endsWith(tecnicoId))
                // Agrupa por sala
                .collect(Collectors.groupingBy(ChatMessageEntity::getRoomId))
                // Transforma cada grupo em um único objeto de "chamado"
                .values().stream()
                .map(mensagensDaSala -> {
                    // Pega a mensagem mais recente para exibir como "lastMessage"
                    ChatMessageEntity ultimaMensagem = mensagensDaSala.stream()
                            .max((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                            .orElse(null);

                    if (ultimaMensagem == null) return null;

                    // Monta o objeto que o frontend espera
                    return Map.of(
                            "usuarioId", ultimaMensagem.getUserId(),
                            "usuarioNome", ultimaMensagem.getUser(),
                            "roomId", ultimaMensagem.getRoomId(),
                            "lastMessage", ultimaMensagem.getMessage(),
                            "timestamp", ultimaMensagem.getTimestamp().toString()
                    );
                })
                .filter(map -> map != null)
                .collect(Collectors.toList());
    }

    // ESTE MÉTODO AGORA APENAS SALVA A MENSAGEM NO BANCO
    public void processarMensagem(ChatMessage message) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setRoomId(message.getRoomId());
        entity.setMessage(message.getMessage());
        entity.setUser(message.getUser());
        entity.setUserId(message.getUserId());
        entity.setTimestamp(LocalDateTime.now());
        // A propriedade 'offline' pode ser usada no futuro se necessário

        chatRepository.save(entity);
    }
}