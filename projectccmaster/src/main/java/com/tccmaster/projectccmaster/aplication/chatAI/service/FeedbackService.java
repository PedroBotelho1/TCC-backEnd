// Project_TccMaster-main/projectccmaster/src/main/java/com/tccmaster/projectccmaster/aplication/chatAI/service/FeedbackService.java
package com.tccmaster.projectccmaster.aplication.chatAI.service;

import com.tccmaster.projectccmaster.aplication.entity.FeedbackEntity;
import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.repository.FeedbackRepository;
import com.tccmaster.projectccmaster.aplication.repository.TecnicoRepository;
import com.tccmaster.projectccmaster.aplication.webSocket.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository; // Adicionado para buscar o técnico

    public FeedbackEntity createFeedback(FeedbackDTO feedbackDTO, UsuarioEntity usuario) {
        // Busca o técnico pelo ID fornecido no DTO
        TecnicoEntity tecnico = tecnicoRepository.findById(feedbackDTO.tecnicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico não encontrado"));

        FeedbackEntity feedback = new FeedbackEntity();

        feedback.setNome(usuario.getNome()); // Pega o nome do usuário autenticado
        feedback.setAvaliacao(feedbackDTO.avaliacao());
        feedback.setComentario(feedbackDTO.comentario());
        feedback.setUsuario(usuario); // Associa o usuário ao feedback
        feedback.setTecnico(tecnico); // Associa o técnico ao feedback
        feedback.setDataCriacao(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    public List<FeedbackEntity> getFeedbacksByUsuario(UUID usuarioId) {
        return feedbackRepository.findByUsuarioId(usuarioId);
    }

    public List<FeedbackEntity> getAllFeedbacks() {
        return feedbackRepository.buscarTodosOsFeedbacks();
    }
}