// Em: src/main/java/com/tccmaster/projectccmaster/aplication/chatAI/service/FeedbackService.java
package com.tccmaster.projectccmaster.aplication.chatAI.service;

import com.tccmaster.projectccmaster.aplication.entity.FeedbackEntity;
import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.repository.FeedbackRepository;
import com.tccmaster.projectccmaster.aplication.repository.TecnicoRepository;
import com.tccmaster.projectccmaster.aplication.webSocket.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails; // IMPORTAR
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private TecnicoRepository tecnicoRepository;

    public FeedbackEntity createFeedback(FeedbackDTO feedbackDTO, UserDetails principal) {
        FeedbackEntity feedback = new FeedbackEntity();

        // --- LÓGICA DE AUTORIA CORRIGIDA ---
        if (principal instanceof UsuarioEntity) {
            feedback.setUsuario((UsuarioEntity) principal);
            feedback.setAutorTecnico(null);
        } else if (principal instanceof TecnicoEntity) {
            if (!"PLATAFORMA".equalsIgnoreCase(feedbackDTO.tipo())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Técnicos só podem enviar feedbacks sobre a plataforma.");
            }
            feedback.setUsuario(null);
            feedback.setAutorTecnico((TecnicoEntity) principal); // <-- CORREÇÃO CRÍTICA: Salva o técnico autor
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tipo de usuário inválido.");
        }

        // ... (o resto do método createFeedback continua igual)
        if ("ATENDIMENTO".equalsIgnoreCase(feedbackDTO.tipo())) {
            if (feedbackDTO.tecnicoId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tecnicoId é obrigatório.");
            }
            TecnicoEntity tecnicoAvaliado = tecnicoRepository.findById(feedbackDTO.tecnicoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico não encontrado"));
            feedback.setTecnico(tecnicoAvaliado);
            feedback.setNome(tecnicoAvaliado.getNome());
        } else if ("PLATAFORMA".equalsIgnoreCase(feedbackDTO.tipo())) {
            feedback.setTecnico(null);
            feedback.setNome("Plataforma");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de feedback inválido.");
        }
        feedback.setAvaliacao(feedbackDTO.avaliacao());
        feedback.setComentario(feedbackDTO.comentario());
        feedback.setDataCriacao(LocalDateTime.now());
        feedback.setTipo(feedbackDTO.tipo().toUpperCase());
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackEntity> getFeedbacksByUsuario(UUID usuarioId) {
        return feedbackRepository.findByUsuarioId(usuarioId);
    }

    public List<FeedbackEntity> getFeedbacksByTecnico(UUID tecnicoId) {
        return feedbackRepository.findByTecnicoId(tecnicoId);
    }

    // --- NOVO MÉTODO ADICIONADO ---
    public List<FeedbackEntity> getFeedbacksSentByTecnico(UUID tecnicoId) {
        return feedbackRepository.findByAutorTecnicoId(tecnicoId);
    }

    public List<FeedbackEntity> getAllFeedbacks() {
        return feedbackRepository.buscarTodosOsFeedbacks();
    }
}