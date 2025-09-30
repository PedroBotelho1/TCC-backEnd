// Project_TccMaster-main/projectccmaster/src/main/java/com/tccmaster/projectccmaster/aplication/controller/FeedbackController.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.chatAI.service.FeedbackService;
import com.tccmaster.projectccmaster.aplication.entity.FeedbackEntity;
import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.webSocket.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Supondo que você use Spring Security
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackEntity> createFeedback(@RequestBody FeedbackDTO feedbackDTO, @AuthenticationPrincipal UsuarioEntity usuario) {
        // O Spring Security (se configurado) irá injetar o usuário autenticado aqui.
        FeedbackEntity feedback = feedbackService.createFeedback(feedbackDTO, usuario);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/me")
    public ResponseEntity<List<FeedbackEntity>> getMeusFeedbacks(@AuthenticationPrincipal UsuarioEntity usuario) {
        List<FeedbackEntity> feedbacks = feedbackService.getFeedbacksByUsuario(usuario.getId());
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<FeedbackEntity>> getAllFeedbacks() {
        List<FeedbackEntity> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }
}