// Arquivo: projectccmaster/src/main/java/com/tccmaster/projectccmaster/aplication/webSocket/dto/ChatMessage.java

package com.tccmaster.projectccmaster.aplication.webSocket.dto;

public class ChatMessage {
    private String id;        // ADICIONADO
    private String message;
    private String user;
    private String userId;    // ADICIONADO
    private String timestamp; // ADICIONADO

    // Adicione construtores, getters e setters para os novos campos

    public ChatMessage() {
    }

    public ChatMessage(String id, String message, String user, String userId, String timestamp) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // --- GETTERS E SETTERS ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}