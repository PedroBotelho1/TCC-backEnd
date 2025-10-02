// Em: src/main/java/com/tccmaster/projectccmaster/aplication/webSocket/dto/FeedbackDTO.java
package com.tccmaster.projectccmaster.aplication.webSocket.dto;

import java.util.UUID;

// MODIFICADO: Removemos 'nome' e adicionamos 'tipo'. 
// 'tecnicoId' pode ser nulo.
public record FeedbackDTO(
        int avaliacao,
        String comentario,
        UUID tecnicoId,
        String tipo
) {}