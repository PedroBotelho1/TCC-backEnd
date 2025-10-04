// Em: src/main/java/com/tccmaster/projectccmaster/aplication/controller/FeedbackController.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.chatAI.service.FeedbackService;
import com.tccmaster.projectccmaster.aplication.entity.FeedbackEntity;
import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.webSocket.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackEntity> createFeedback(@RequestBody FeedbackDTO feedbackDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // A lógica de permissão foi movida para o FeedbackService.
        FeedbackEntity feedback = feedbackService.createFeedback(feedbackDTO, userDetails);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/me")
    public ResponseEntity<List<FeedbackEntity>> getMeusFeedbacks(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails instanceof UsuarioEntity) {
            // Para um usuário, busca os feedbacks que ele enviou
            UsuarioEntity usuario = (UsuarioEntity) userDetails;
            List<FeedbackEntity> feedbacks = feedbackService.getFeedbacksByUsuario(usuario.getId());
            return ResponseEntity.ok(feedbacks);
        } else if (userDetails instanceof TecnicoEntity) {
            // --- CORREÇÃO APLICADA AQUI ---
            // Para um técnico, busca os feedbacks que ele enviou (e não os que recebeu)
            TecnicoEntity tecnico = (TecnicoEntity) userDetails;
            List<FeedbackEntity> feedbacks = feedbackService.getFeedbacksSentByTecnico(tecnico.getId());
            return ResponseEntity.ok(feedbacks);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/todos")
    public ResponseEntity<List<FeedbackEntity>> getAllFeedbacks() {
        List<FeedbackEntity> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }
}