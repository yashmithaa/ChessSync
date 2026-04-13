package com.chess.controller;

import com.chess.model.Player;
import com.chess.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long id) {
        Optional<Player> player = playerService.getPlayerById(id);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        try {
            Optional<Player> optionalPlayer = playerService.getPlayerById(id);
            if (optionalPlayer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
            }

            Player player = optionalPlayer.get();

            // Apply updates
            if (updates.containsKey("username")) {
                player.setUsername(updates.get("username"));
            }
            if (updates.containsKey("email")) {
                player.setEmail(updates.get("email"));
            }
            if (updates.containsKey("password")) {
                player.setPassword(updates.get("password"));
            }

            playerService.updatePlayer(player);

            Map<String, Object> response = new HashMap<>();
            response.put("id", player.getId());
            response.put("username", player.getUsername());
            response.put("email", player.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update player: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPlayers(@RequestParam String query) {
        try {
            List<Player> players = playerService.getAllPlayers().stream()
                    .filter(p -> p.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                                 p.getEmail().toLowerCase().contains(query.toLowerCase()))
                    .toList();

            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search players: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getPlayerStats(@PathVariable Long id) {
        try {
            Optional<Player> optionalPlayer = playerService.getPlayerById(id);
            if (optionalPlayer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
            }

            Player player = optionalPlayer.get();
            Map<String, Object> stats = new HashMap<>();
            stats.put("id", player.getId());
            stats.put("username", player.getUsername());
            stats.put("rating", player.getRating());
            stats.put("gamesPlayed", player.getGamesPlayed());
            stats.put("gamesWon", player.getGamesWon());
            stats.put("winRate", player.getGamesPlayed() > 0
                    ? String.format("%.2f", (100.0 * player.getGamesWon() / player.getGamesPlayed())) + "%"
                    : "0%");

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve player stats: " + e.getMessage());
        }
    }
}
// Last modified during: feat: Add REST API controllers and endpoints [minor]
