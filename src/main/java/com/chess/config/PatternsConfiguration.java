package com.chess.config;

import com.chess.model.pieces.factory.PieceFactory;
import com.chess.model.pieces.factory.StandardPieceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Spring Configuration for Design Patterns
 * 
 * Demonstrates:
 * - Factory Pattern with DIP
 * - Bean management for different implementations
 * - Dependency injection of abstract types
 */
@Configuration
public class PatternsConfiguration {

    /**
     * Provides the standard piece factory (creates basic pieces without decorators)
     */
    @Bean(name = "standardFactory")
    @Primary
    public PieceFactory standardPieceFactory() {
        return new StandardPieceFactory();
    }

    /**
     * Default bean
     * Classes can inject PieceFactory directly:
     *     @Autowired
     *     private PieceFactory pieceFactory;
     */
    @Bean
    public PieceFactory pieceFactory() {
        return standardPieceFactory();
    }
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
