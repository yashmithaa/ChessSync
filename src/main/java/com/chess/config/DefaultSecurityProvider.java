package com.chess.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Dependency Inversion Principle (DIP):
 * Concrete implementation of SecurityProvider.
 * Provides security beans while maintaining abstraction.
 */
@Component
public class DefaultSecurityProvider implements SecurityProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
