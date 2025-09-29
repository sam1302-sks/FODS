package com.example.Telemedi.Controller;

import com.example.Telemedi.dto.QueryRequest;
import com.example.Telemedi.dto.BotResponse;
import com.example.Telemedi.entity.ChatMessage;
import com.example.Telemedi.entity.ChatSession;
import com.example.Telemedi.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/ask")
    public ResponseEntity<BotResponse> askQuestion(@RequestBody QueryRequest request) {
        try {
            BotResponse response = chatBotService.processQuery(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BotResponse errorResponse = new BotResponse();
            errorResponse.setMessage("I'm sorry, I encountered an error. Please try again.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/ask/{userEmail}")
    public ResponseEntity<BotResponse> askQuestionWithUser(
            @RequestBody QueryRequest request,
            @PathVariable String userEmail) {
        try {
            BotResponse response = chatBotService.processQuery(request, userEmail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BotResponse errorResponse = new BotResponse();
            errorResponse.setMessage("I'm sorry, I encountered an error. Please try again.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/start")
    public ResponseEntity<BotResponse> startConversation() {
        QueryRequest request = new QueryRequest(null, "");
        BotResponse response = chatBotService.processQuery(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{userEmail}")
    public ResponseEntity<List<ChatSession>> getChatHistory(@PathVariable String userEmail) {
        try {
            List<ChatSession> sessions = chatBotService.getUserChatHistory(userEmail);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/session/{sessionId}/messages/{userEmail}")
    public ResponseEntity<List<ChatMessage>> getSessionMessages(
            @PathVariable String sessionId,
            @PathVariable String userEmail) {
        try {
            List<ChatMessage> messages = chatBotService.getSessionMessages(sessionId, userEmail);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
