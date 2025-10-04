// Em: src/main/java/com/tccmaster/projectccmaster/aplication/chatAI/service/ChatService.java

package com.tccmaster.projectccmaster.aplication.chatAI.service;

import com.tccmaster.projectccmaster.aplication.chatAI.form.ChatForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    public ChatForm generateContent(String requestBody) {

        // CORREÇÃO FINAL: Usando o nome exato do modelo da sua lista.
        String modelName = "gemini-2.0-flash";

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
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