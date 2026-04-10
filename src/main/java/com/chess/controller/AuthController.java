package com.chess.controller;

import com.chess.model.Player;
import com.chess.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            
            if (username == null || email == null || password == null) {
                return ResponseEntity.badRequest().body("Username, email, and password are required");
            }
            
            Player player = authService.registerPlayer(username, email, password);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", player.getId());
            response.put("username", player.getUsername());
            response.put("email", player.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }
            
            Optional<Player> player = authService.authenticatePlayer(username, password);
            
            if (player.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", player.get().getId());
                response.put("username", player.get().getUsername());
                response.put("email", player.get().getEmail());
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }
}// Last modified during: feat: Implement AuthService with Single Responsibility Principle [SRP]
