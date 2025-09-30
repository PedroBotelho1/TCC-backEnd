package com.tccmaster.projectccmaster.aplication.webSocket.controller;

import com.tccmaster.projectccmaster.aplication.webSocket.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message){
        // CORREÇÃO:
        // Agora, o objeto 'message' recebido do front-end já contém todos os campos.
        // Apenas o retornamos como está, e ele será transmitido para todos os clientes
        // com os dados completos (incluindo id, userId e timestamp).
        return message;
    }
}
