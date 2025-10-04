package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.Service.ChatManagementService;
import com.tccmaster.projectccmaster.aplication.repository.ChatRepository; // ADICIONADO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // ADICIONADO
import org.springframework.transaction.annotation.Transactional; // ADICIONADO
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatManagementService chatManagementService;

    // NOVO REPOSITÓRIO INJETADO
    @Autowired
    private ChatRepository chatRepository;

    /**
     * Endpoint para o técnico buscar a lista de chamados (conversas) abertos.
     * @param tecnicoId O ID do técnico logado.
     * @return Uma lista de conversas, cada uma representada por um mapa.
     */
    @GetMapping("/usuarios/{tecnicoId}")
    public List<Map<String, String>> getUsuariosParaTecnico(@PathVariable String tecnicoId) {
        return chatManagementService.getConversasParaTecnico(tecnicoId);
    }

    // NOVO ENDPOINT PARA FINALIZAR O ATENDIMENTO
    @PostMapping("/{roomId}/finalizar")
    @Transactional
    public ResponseEntity<Void> finalizarAtendimento(@PathVariable String roomId) {
        chatRepository.deleteByRoomId(roomId);
        return ResponseEntity.ok().build();
    }
}
