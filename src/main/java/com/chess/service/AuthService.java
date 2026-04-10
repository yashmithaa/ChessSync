package com.chess.service;

import com.chess.model.Player;
import com.chess.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]?\\$\\d{2}\\$.{53}$");

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Player registerPlayer(String username, String email, String password) {
        if (playerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (playerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Player player = new Player(username, passwordEncoder.encode(password), email);
        return playerRepository.save(player);
    }

    public Optional<Player> authenticatePlayer(String username, String password) {
        Optional<Player> playerOpt = playerRepository.findByUsername(username);

        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            String storedPassword = player.getPassword();
            if (storedPassword == null || storedPassword.isBlank()) {
                return Optional.empty();
            }

            // Support legacy plaintext passwords and transparently migrate on successful login.
            if (isBcryptHash(storedPassword)) {
                if (passwordEncoder.matches(password, storedPassword)) {
                    return Optional.of(player);
                }
            } else if (password.equals(storedPassword)) {
                player.setPassword(passwordEncoder.encode(password));
                playerRepository.save(player);
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    private boolean isBcryptHash(String value) {
        return BCRYPT_PATTERN.matcher(value).matches();
    }

    public boolean sendPasswordResetLink(String email) {
        Optional<Player> playerOpt = playerRepository.findByEmail(email);
        return playerOpt.isPresent(); // Simulate sending email
    }
}
// Last modified during: feat: Create Player entity and AuthService foundation [major]
// Last modified during: feat: Implement EncryptionService singleton for password hashing [singleton]
