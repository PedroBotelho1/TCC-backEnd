// ChatController.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.webSocket.dto.ChatMessage;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    // Em produção, use um repositório JPA para salvar no banco
    private Map<String, List<Map<String, String>>> conversasPorTecnico = new HashMap<>();

    @GetMapping("/usuarios/{tecnicoId}")
    public List<Map<String, String>> getUsuariosParaTecnico(@PathVariable String tecnicoId) {
        return conversasPorTecnico.getOrDefault(tecnicoId, new ArrayList<>());
    }

    @PostMapping("/registrar-conversa")
    public void registrarConversa(@RequestBody Map<String, String> dadosConversa) {
        String tecnicoId = dadosConversa.get("tecnicoId");
        String usuarioId = dadosConversa.get("usuarioId");
        String usuarioNome = dadosConversa.get("usuarioNome");
        String roomId = dadosConversa.get("roomId");

        List<Map<String, String>> conversas = conversasPorTecnico.getOrDefault(tecnicoId, new ArrayList<>());

        // Verifica se já existe conversa com este usuário
        boolean conversaExistente = conversas.stream()
                .anyMatch(conv -> conv.get("usuarioId").equals(usuarioId));

        if (!conversaExistente) {
            Map<String, String> novaConversa = new HashMap<>();
            novaConversa.put("usuarioId", usuarioId);
            novaConversa.put("usuarioNome", usuarioNome);
            novaConversa.put("roomId", roomId);
            novaConversa.put("lastMessage", "Nova conversa iniciada");
            novaConversa.put("timestamp", new Date().toString());

            conversas.add(novaConversa);
            conversasPorTecnico.put(tecnicoId, conversas);
        }
    }

    // ChatController.java
    @GetMapping("/{roomId}/messages")
    public List<ChatMessage> getHistoricoMensagens(@PathVariable String roomId) {
        // Em um sistema real, você buscaria do banco de dados
        // return chatRepository.findByRoomIdOrderByTimestamp(roomId);

        // Mock temporário - substitua pela implementação real
        List<ChatMessage> mensagens = new ArrayList<>();

        // Adicione aqui a lógica para buscar do banco de dados
        // Exemplo:
        // mensagens = chatService.getMensagensPorSala(roomId);

        return mensagens;
    }
}