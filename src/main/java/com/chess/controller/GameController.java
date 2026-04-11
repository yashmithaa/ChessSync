package com.chess.controller;

import com.chess.model.Game;
import com.chess.model.GameMode;
import com.chess.model.Move;
import com.chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createGame(@RequestBody Map<String, Object> request) {
        try {
            Long whitePlayerId = Long.valueOf(request.get("whitePlayerId").toString());
            Long blackPlayerId = Long.valueOf(request.get("blackPlayerId").toString());
            GameMode gameMode = GameMode.valueOf(request.get("gameMode").toString());
            
            Game game = gameService.createGame(whitePlayerId, blackPlayerId, gameMode);
            
            Map<String, Object> response = new HashMap<>();
            response.put("gameId", game.getId());
            response.put("status", game.getStatus());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create game: " + e.getMessage());
        }
    }
    
    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable Long gameId) {
        try {
            Optional<Game> game = gameService.getGame(gameId);
            
            if (game.isPresent()) {
                return ResponseEntity.ok(game.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve game: " + e.getMessage());
        }
    }
    
    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody Move move) {
        try {
            boolean success = gameService.makeMove(gameId, move);
            
            if (success) {
                Optional<Game> updatedGame = gameService.getGame(gameId);
                return ResponseEntity.ok(updatedGame.get());
            } else {
                return ResponseEntity.badRequest().body("Invalid move");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to make move: " + e.getMessage());
        }
    }
    
    @GetMapping("/player/{playerId}")
    public ResponseEntity<?> getPlayerGames(@PathVariable Long playerId) {
        try {
            List<Game> games = gameService.getPlayerGames(playerId);
            return ResponseEntity.ok(games);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve player games: " + e.getMessage());
        }
    }
    
    @PostMapping("/{gameId}/resign")
    public ResponseEntity<?> resignGame(@PathVariable Long gameId, @RequestBody Map<String, Long> request) {
        try {
            Long playerId = request.get("playerId");
            boolean success = gameService.resignGame(gameId, playerId);
            
            if (success) {
                Optional<Game> game = gameService.getGame(gameId);
                return ResponseEntity.ok(game.get());
            } else {
                return ResponseEntity.badRequest().body("Failed to resign game");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing resignation: " + e.getMessage());
        }
    }
}// Last modified during: feat: Add GameController and game orchestration endpoints [minor]
