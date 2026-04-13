package com.chess.service;

import com.chess.model.Player;
import com.chess.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PlayerService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]?\\$\\d{2}\\$.{53}$");
    
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
    
    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }
    
    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }
    
    public void updatePlayerStats(Player player, boolean won) {
        player.setGamesPlayed(player.getGamesPlayed() + 1);
        if (won) {
            player.setGamesWon(player.getGamesWon() + 1);
            player.setRating(player.getRating() + 10); // Simple rating adjustment
        } else {
            player.setRating(Math.max(1000, player.getRating() - 5)); // Prevent rating from going too low
        }
        normalizePasswordIfNeeded(player);
        playerRepository.save(player);
    }
    
    public void updatePlayer(Player player) {
        normalizePasswordIfNeeded(player);
        playerRepository.save(player);
    }

    private void normalizePasswordIfNeeded(Player player) {
        String currentPassword = player.getPassword();
        if (currentPassword == null || currentPassword.isBlank()) {
            return;
        }

        if (!BCRYPT_PATTERN.matcher(currentPassword).matches()) {
            player.setPassword(passwordEncoder.encode(currentPassword));
        }
    }
}// Last modified during: feat: Create Player management service and repository [major]
