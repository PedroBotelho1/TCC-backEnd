package com.tccmaster.projectccmaster.aplication.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mensagens_chat")
public class ChatMessageEntity {

    // --- CORREÇÃO ADICIONADA AQUI ---
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    // --- FIM DA CORREÇÃO ---

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(name = "mensagem", nullable = false, length = 1000)
    private String message;

    @Column(name = "usuario", nullable = false)
    private String user;

    @Column(name = "usuario_id", nullable = false)
    private String userId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "offline")
    private Boolean offline = false;

    // Construtores, getters e setters
    public ChatMessageEntity() {}

    public ChatMessageEntity(String roomId, String message, String user, String userId) {
        this.roomId = roomId;
        this.message = message;
        this.user = user;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }

    // --- GETTERS E SETTERS PARA O ID ADICIONADOS ---
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    // --- FIM DA ADIÇÃO ---


    // Getters e Setters existentes...
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Boolean getOffline() { return offline; }
    public void setOffline(Boolean offline) { this.offline = offline; }
}
