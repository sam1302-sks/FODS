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
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/ask")
    public ResponseEntity<BotResponse> askQuestion(@RequestBody QueryRequest request) {
        try {
            BotResponse response = chatBotService.processQuery(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            BotResponse errorResponse = new BotResponse();
            errorResponse.setMessage("I'm sorry, I encountered an error. Please try again.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/ask/{email}")
    public ResponseEntity<BotResponse> askQuestionWithUser(
            @RequestBody QueryRequest request,
            @PathVariable String email) {
        try {
            System.out.println("📩 Received query from: " + email);
            System.out.println("📝 SessionId: " + request.getSessionId());
            System.out.println("📝 Message: " + request.getMessage());

            BotResponse response = chatBotService.processQuery(request, email);

            System.out.println("✅ Response message: " + response.getMessage());
            System.out.println("💊 Remedy: " + response.getRemedy());
            System.out.println("🔚 Is end: " + response.isConversationEnd());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            BotResponse errorResponse = new BotResponse();
            errorResponse.setMessage("I'm sorry, I encountered an error. Please try again.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/start")
    public ResponseEntity<BotResponse> startConversation() {
        try {
            QueryRequest request = new QueryRequest(null, "");
            BotResponse response = chatBotService.processQuery(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            BotResponse errorResponse = new BotResponse();
            errorResponse.setMessage("Hello! I'm your medical assistant. How can I help you today?");
            return ResponseEntity.ok(errorResponse);
        }
    }

    @GetMapping("/history/{email}")
    public ResponseEntity<List<ChatSession>> getChatHistory(@PathVariable String email) {
        try {
            System.out.println("📚 Loading history for: " + email);
            List<ChatSession> sessions = chatBotService.getUserChatHistory(email);
            System.out.println("✅ Found " + sessions.size() + " sessions");
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable String sessionId) {
        try {
            System.out.println("🗑️ Deleting session: " + sessionId);
            boolean success = chatBotService.deleteChatSessionById(sessionId);
            if (success) {
                System.out.println("✅ Session deleted successfully");
                return ResponseEntity.ok().build();
            } else {
                System.out.println("❌ Failed to delete session");
                return ResponseEntity.status(404).body("Session not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete session: " + e.getMessage());
        }
    }

    @GetMapping("/session/{sessionId}/messages/{email}")
    public ResponseEntity<List<ChatMessage>> getSessionMessages(
            @PathVariable String sessionId,
            @PathVariable String email) {
        try {
            System.out.println("📨 Loading messages for session: " + sessionId + ", user: " + email);
            List<ChatMessage> messages = chatBotService.getSessionMessages(sessionId, email);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
