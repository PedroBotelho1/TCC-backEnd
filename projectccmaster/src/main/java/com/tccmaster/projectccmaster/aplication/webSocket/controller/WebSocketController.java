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
        // Extrai o ID do técnico a partir do ID da sala
        String tecnicoId = extrairTecnicoIdDoRoomId(roomId);

        // Adiciona o roomId ao objeto da mensagem para ser usado no service
        message.setRoomId(roomId);

        // Notifica o serviço para processar a mensagem (criar ou atualizar o chamado)
        chatManagementService.processarMensagem(tecnicoId, message);

        // Retorna a mensagem para ser enviada a todos os inscritos no tópico
        return message;
    }

    /**
     * CORREÇÃO: Método robusto para extrair o ID do técnico do ID da sala.
     * Assume que o formato do roomId é "prefixo-uuidUsuario-uuidTecnico".
     * @param roomId O ID da sala de chat.
     * @return O ID do técnico (os últimos 36 caracteres).
     */
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

