package com.tccmaster.projectccmaster.aplication.webSocket.dto;

import java.util.UUID;

public record FeedbackDTO(
        String nome,
        int avaliacao,
        String comentario,
        UUID tecnicoId
) {}
