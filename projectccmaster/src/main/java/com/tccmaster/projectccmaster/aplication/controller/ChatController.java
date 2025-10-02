package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.Service.ChatManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatManagementService chatManagementService;

    /**
     * Endpoint para o técnico buscar a lista de chamados (conversas) abertos.
     * @param tecnicoId O ID do técnico logado.
     * @return Uma lista de conversas, cada uma representada por um mapa.
     */
    @GetMapping("/usuarios/{tecnicoId}")
    public List<Map<String, String>> getUsuariosParaTecnico(@PathVariable String tecnicoId) {
        return chatManagementService.getConversasParaTecnico(tecnicoId);
    }

    // O endpoint POST /registrar-conversa foi REMOVIDO.
}
