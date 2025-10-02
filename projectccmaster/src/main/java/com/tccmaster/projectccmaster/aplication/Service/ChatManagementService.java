package com.tccmaster.projectccmaster.aplication.Service;

import com.tccmaster.projectccmaster.aplication.webSocket.dto.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatManagementService {

    // Usando ConcurrentHashMap para segurança em ambientes com múltiplas threads
    private final Map<String, List<Map<String, String>>> conversasPorTecnico = new ConcurrentHashMap<>();

    /**
     * Retorna a lista de conversas (chamados) para um técnico específico.
     * @param tecnicoId O ID do técnico.
     * @return A lista de conversas.
     */
    public List<Map<String, String>> getConversasParaTecnico(String tecnicoId) {
        return conversasPorTecnico.getOrDefault(tecnicoId, new ArrayList<>());
    }

    /**
     * Processa uma mensagem recebida via WebSocket.
     * Se for a primeira mensagem de um usuário para um técnico, cria um novo "chamado".
     * Se não, apenas atualiza a última mensagem e o timestamp.
     * @param tecnicoId O ID do técnico associado à sala.
     * @param message O objeto da mensagem recebida.
     */
    public void processarMensagem(String tecnicoId, ChatMessage message) {
        if (tecnicoId == null || tecnicoId.trim().isEmpty()) {
            return; // Não faz nada se não houver um ID de técnico
        }

        List<Map<String, String>> conversas = conversasPorTecnico.computeIfAbsent(tecnicoId, k -> new ArrayList<>());

        // Procura se já existe uma conversa com este usuário
        var conversaExistente = conversas.stream()
                .filter(conv -> conv.get("usuarioId").equals(message.getUserId()))
                .findFirst();

        if (conversaExistente.isPresent()) {
            // Se a conversa já existe, atualiza a última mensagem e o timestamp
            Map<String, String> conversa = conversaExistente.get();
            conversa.put("lastMessage", message.getMessage());
            conversa.put("timestamp", new Date().toString());
        } else {
            // Se for a primeira mensagem, cria um novo registro de conversa
            Map<String, String> novaConversa = new HashMap<>();
            novaConversa.put("usuarioId", message.getUserId());
            novaConversa.put("usuarioNome", message.getUser());
            novaConversa.put("roomId", message.getRoomId()); // Armazena o roomId
            novaConversa.put("lastMessage", message.getMessage());
            novaConversa.put("timestamp", new Date().toString());
            conversas.add(novaConversa);
        }
    }
}
