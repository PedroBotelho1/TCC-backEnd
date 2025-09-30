package com.tccmaster.projectccmaster.aplication.chatAI.service;

import com.tccmaster.projectccmaster.aplication.chatAI.form.ChatForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Make sure this import is here
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    @Autowired
    private RestTemplate restTemplate;

    // This reads the key from application.properties
    @Value("${gemini.api.key}")
    private String apiKey;

    public ChatForm generateContent(String requestBody) {
        // Correct Model Name and URL construction
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, // Using the corrected URL
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray sugestoes = jsonResponse.getJSONArray("candidates");
                if (sugestoes.length() > 0) {
                    JSONObject candidate = sugestoes.getJSONObject(0);
                    JSONObject content = candidate.getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");
                    if (parts.length() > 0) {
                        ChatForm message = new ChatForm();
                        String rawText = parts.getJSONObject(0).getString("text");

                        String formattedText = rawText
                                .replace("#", "")
                                .replace("*", "")
                                .replaceAll("\\\\n", "\n")
                                .replaceAll("\\\\t", "\t");

                        message.setMessage(formattedText);
                        return message;
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException("Falha ao analisar a resposta: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Falha ao chamar a API:" + response.getStatusCode());
        }

        return null;
    }
}