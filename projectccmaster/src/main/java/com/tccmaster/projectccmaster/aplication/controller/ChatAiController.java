package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.chatAI.form.ChatForm;
import com.tccmaster.projectccmaster.aplication.chatAI.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tech-help")
public class ChatAiController {

    static final String MENSAGEM_INICIAL = "Aja como um especialista em suporte técnico de hardware e software. Forneça uma ou mais possíveis causas e soluções para o seguinte problema: ";

    @Autowired
    private ChatService chatService;

    @PostMapping("/solucao-ia")
    public ChatForm generateContent(@RequestBody ChatForm message) {
        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + MENSAGEM_INICIAL + message.getMessage() + "\"}]}]}";
        return chatService.generateContent(requestBody);
    }
}