package com.tccmaster.projectccmaster.aplication.webSocket.controller;

import com.tccmaster.projectccmaster.aplication.Service.ChatManagementService;
import com.tccmaster.projectccmaster.aplication.webSocket.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {

    @Autowired
    private ChatManagementService chatManagementService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) {
        // Adiciona o roomId na mensagem
        message.setRoomId(roomId);

        // Apenas processa e salva a mensagem no banco
        chatManagementService.processarMensagem(message);

        // Retorna a mensagem para todos os inscritos
        return message;
    }

    // O método extrairTecnicoIdDoRoomId não é mais necessário aqui.

    private String extrairTecnicoIdDoRoomId(String roomId) {
        // A lógica assume que o ID do técnico são os últimos 36 caracteres do roomId,
        // que corresponde ao formato de um UUID.
        if (roomId != null && roomId.length() > 36) {
            return roomId.substring(roomId.length() - 36);
        }
        // Adiciona um log no console do servidor para ajudar a depurar caso falhe
        System.err.println("AVISO: Não foi possível extrair o ID do técnico do roomId: " + roomId);
        return null;
    }
}

