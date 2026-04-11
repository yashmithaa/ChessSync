package com.chess.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Dependency Inversion Principle (DIP):
 * Interface for providing security-related configurations.
 * Services depend on this abstraction, not directly on PasswordEncoder implementations.
 * 
 * This decouples the service layer from concrete security implementations.
 */
public interface SecurityProvider {
    /**
     * Get the password encoder to use in the application
     * @return A PasswordEncoder instance
     */
    PasswordEncoder getPasswordEncoder();
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
