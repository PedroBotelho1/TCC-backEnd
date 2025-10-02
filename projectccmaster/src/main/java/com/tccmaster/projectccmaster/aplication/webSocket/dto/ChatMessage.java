package com.tccmaster.projectccmaster.aplication.webSocket.dto;

public class ChatMessage {
    private String id;
    private String message;
    private String user;
    private String userId;
    private String timestamp;
    private String roomId; // CAMPO ADICIONADO

    // Construtores...
    public ChatMessage() {}

    // Getters e Setters (incluindo para roomId)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}
