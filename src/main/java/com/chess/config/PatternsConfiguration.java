package com.chess.config;

import com.chess.model.pieces.factory.DecoratedPieceFactory;
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
    public PieceFactory standardPieceFactory() {
        return new StandardPieceFactory();
    }

    /**
     * Provides the decorated piece factory (creates pieces with special move decorators)
     * This is marked as @Primary, so it's used by default in autowiring
     */
    @Bean(name = "decoratedFactory")
    @Primary
    public PieceFactory decoratedPieceFactory() {
        return new DecoratedPieceFactory();
    }

    /**
     * Default bean - uses the enhanced decorated factory
     * Classes can inject PieceFactory directly:
     *     @Autowired
     *     private PieceFactory pieceFactory;  // Gets decoratedFactory
     */
    @Bean
    public PieceFactory pieceFactory() {
        return decoratedPieceFactory();
    }
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
