package com.example.Telemedi.Controller;

import com.example.Telemedi.dto.*;
import com.example.Telemedi.entity.User;
import com.example.Telemedi.service.UserService;
import com.example.Telemedi.service.ChatHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFullName()
            );

            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
                userService.updateUser(user);
            }

            UserResponse response = new UserResponse(user.getUserId(), user.getEmail(), user.getFullName());
            response.setCreatedAt(user.getCreatedAt());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.authenticateUser(request.getEmail(), request.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            UserResponse response = new UserResponse(user.getUserId(), user.getEmail(), user.getFullName());
            response.setPhone(user.getPhone());
            response.setCreatedAt(user.getCreatedAt());
            response.setLastLogin(user.getLastLogin());
            response.setTotalSessions(chatHistoryService.getUserTotalSessions(user));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<?> getUserProfile(@PathVariable String email) {
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserResponse response = new UserResponse(user.getUserId(), user.getEmail(), user.getFullName());
            response.setPhone(user.getPhone());
            response.setCreatedAt(user.getCreatedAt());
            response.setLastLogin(user.getLastLogin());
            response.setTotalSessions(chatHistoryService.getUserTotalSessions(user));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
