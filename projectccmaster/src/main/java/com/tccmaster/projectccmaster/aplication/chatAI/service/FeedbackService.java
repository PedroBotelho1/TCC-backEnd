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
        FeedbackEntity feedback = new FeedbackEntity();

        // Lógica condicional baseada no tipo de feedback
        if ("ATENDIMENTO".equalsIgnoreCase(feedbackDTO.tipo())) {
            if (feedbackDTO.tecnicoId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tecnicoId é obrigatório para feedback de atendimento.");
            }
            // Busca o técnico pelo ID fornecido no DTO
            TecnicoEntity tecnico = tecnicoRepository.findById(feedbackDTO.tecnicoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico não encontrado"));

            feedback.setTecnico(tecnico); // Associa o técnico ao feedback
            feedback.setNome(tecnico.getNome()); // Guarda o nome do técnico para fácil acesso

        } else if ("PLATAFORMA".equalsIgnoreCase(feedbackDTO.tipo())) {
            feedback.setTecnico(null); // Nenhum técnico associado
            feedback.setNome("Plataforma"); // Indica que o feedback é sobre a plataforma

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de feedback inválido.");
        }

        feedback.setAvaliacao(feedbackDTO.avaliacao());
        feedback.setComentario(feedbackDTO.comentario());
        feedback.setUsuario(usuario); // Associa o usuário que enviou o feedback
        feedback.setDataCriacao(LocalDateTime.now());
        feedback.setTipo(feedbackDTO.tipo().toUpperCase()); // Salva o tipo

        return feedbackRepository.save(feedback);
    }

    public List<FeedbackEntity> getFeedbacksByUsuario(UUID usuarioId) {
        return feedbackRepository.findByUsuarioId(usuarioId);
    }

    public List<FeedbackEntity> getAllFeedbacks() {
        return feedbackRepository.buscarTodosOsFeedbacks();
    }
}