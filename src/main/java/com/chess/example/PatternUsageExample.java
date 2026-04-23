package com.chess.example;

import com.chess.model.*;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Pawn;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.PieceType;
import com.chess.model.pieces.factory.PieceFactory;
import com.chess.model.pieces.factory.StandardPieceFactory;
import com.chess.service.facade.GameFacade;
import com.chess.service.facade.GameState;

import java.util.Optional;

/**
 * Example usage of Structural Patterns and SOLID Principles
 * implemented in the ChessSync application.
 * 
 * This class demonstrates:
 * 1. Decorator Pattern - Adding special moves to pieces
 * 2. Facade Pattern - Simplified game operations
 * 3. Dependency Inversion Principle - Using factories
 * 4. Interface Segregation Principle - Using segregated interfaces
 * 5. Liskov Substitution Principle - Polymorphic piece usage
 */
public class PatternUsageExample {

    /**
     * Example 1: DECORATOR PATTERN
     * Shows how to dynamically add special move capabilities to pieces
     */
    public static void decoratorPatternExample() {
        System.out.println("=== DECORATOR PATTERN EXAMPLE ===\n");
        System.out.println("Decorators have been removed to simplify the codebase.\n");
    }

    /**
     * Example 2: DEPENDENCY INVERSION PRINCIPLE with FACTORIES
     * Shows how different factories create pieces differently
     */
    public static void dipFactoryExample() {
        System.out.println("=== DEPENDENCY INVERSION PRINCIPLE Example ===\n");

        Board board = new Board();

        // StandardPieceFactory - creates basic pieces
        PieceFactory standardFactory = new StandardPieceFactory();
        var basicKing = standardFactory.createPiece(PieceType.KING, PieceColor.WHITE, 7, 4);
        System.out.println("Basic Factory - King moves: " + basicKing.getValidMoves(board).size());
    }

    /**
     * Example 3: INTERFACE SEGREGATION PRINCIPLE
     * Shows how clients depend on only the interfaces they need
     */
    public static void ispExample() {
        System.out.println("=== INTERFACE SEGREGATION PRINCIPLE Example ===\n");

        Board board = new Board();
        King king = new King(PieceColor.WHITE, 7, 4);

        // A move validator only cares about MoveValidator interface
        // It doesn't need to know about getValidMoves()
        boolean isValidMove = king.isValidMove(board, 6, 4);
        System.out.println("King move (6,4) is valid: " + isValidMove);

        // A move generator only cares about MoveProvider interface
        // It doesn't need to know about isValidMove()
        var validMoves = king.getValidMoves(board);
        System.out.println("King has " + validMoves.size() + " valid moves");

        // BENEFIT:
        // - UI can depend on MoveValidator without knowing about MoveProvider
        // - Engine can depend on MoveProvider without knowing about MoveValidator
        // - Reduces unnecessary coupling\n");
    }

    /**
     * Example 4: LISKOV SUBSTITUTION PRINCIPLE
     * Shows polymorphic usage of different piece types
     */
    public static void lspExample() {
        System.out.println("=== LISKOV SUBSTITUTION PRINCIPLE Example ===\n");

        Board board = new Board();

        // LSP: All piece types are substitutable - can use them polymorphically
        var pieces = new java.util.ArrayList<com.chess.model.pieces.Piece>();
        pieces.add(new King(PieceColor.WHITE, 7, 4));
        pieces.add(new Pawn(PieceColor.WHITE, 6, 0));
        pieces.add(new com.chess.model.pieces.Knight(PieceColor.WHITE, 7, 1));

        // All pieces work the same way - polymorphistically
        for (var piece : pieces) {
            System.out.println(piece + " at (" + piece.getRow() + "," + piece.getColumn() + 
                             ") has " + piece.getValidMoves(board).size() + " valid moves");
        }

        System.out.println("\nLSP GUARANTEE:");
        System.out.println("- hasMoved() works the same for all pieces");
        System.out.println("- move() updates position consistently");
        System.out.println("- isValidMove() follows the same semantics");
        System.out.println("- No piece violates the Piece contract\n");
    }

    /**
     * Example 5: FACADE PATTERN
     * Shows simplified game operations without exposing complexity
     */
    public static void facadePatternExample(GameFacade gameFacade) {
        System.out.println("=== FACADE PATTERN Example ===\n");

        // Without Facade (complex, requires orchestration):
        // Player white = playerService.getPlayerById(1).orElse(null);
        // Player black = playerService.getPlayerById(2).orElse(null);
        // if (white != null && black != null) {
        //     Game game = gameService.createGame(white, black, PLAYER_VS_PLAYER);
        // }

        // With Facade (simple, single method call):
        Game game = gameFacade.initializeGame(1L, 2L, GameMode.PLAYER_VS_PLAYER);

        if (game != null) {
            System.out.println("Game created: " + game.getId());

            // Create a sample move
            var piece = game.getBoard().getPiece(6, 0);
            if (piece != null) {
                Move move = new Move(piece, 6, 0, 5, 0);

                // Execute move with validation - all handled by facade
                boolean success = gameFacade.executeMoveWithValidation(game.getId(), move);
                System.out.println("Move executed: " + success);

                // Get complete game state - single call
                Optional<GameState> gameState = gameFacade.getCompleteGameState(game.getId());
                gameState.ifPresent(state -> {
                    System.out.println("Current turn: " + state.getCurrentTurn());
                    System.out.println("White player: " + state.getWhitePlayerName());
                    System.out.println("Black player: " + state.getBlackPlayerName());
                });
            }
        }

        System.out.println("\nFACADE BENEFITS:");
        System.out.println("- Simple, easy-to-use API for clients");
        System.out.println("- Encapsulates GameService, PlayerService, MoveValidator");
        System.out.println("- Controllers don't orchestrate multiple services");
        System.out.println("- Changes to services don't affect client code\n");
    }

    /**
     * Example 6: COMBINING ALL PATTERNS
     * Real-world scenario using all patterns together
     */
    public static void combinedPatternExample(GameFacade gameFacade) {
        System.out.println("=== COMBINED PATTERNS Example ===\n");

        // 1. Initialize game using Facade
        Game game = gameFacade.initializeGame(1L, 2L, GameMode.PLAYER_VS_PLAYER);

        if (game != null) {
            // 2. Get board with decorator-enhanced pieces (DIP + Decorator)
            Board board = game.getBoard();

            // 3. Get king with castling decorator (Decorator Pattern)
            var whiteKing = board.getPiece(7, 4);
            if (whiteKing != null) {
                System.out.println("White King at (7,4)");
                System.out.println("  - Valid moves: " + whiteKing.getValidMoves(board).size());
                
                // 4. Interface Segregation - check validation separately
                boolean canMove = whiteKing.isValidMove(board, 6, 4);
                System.out.println("  - Can move to (6,4): " + canMove);

                // 5. Liskov Substitution - all pieces work the same way
                System.out.println("  - Has moved: " + whiteKing.hasMoved());
            }

            // 6. Make a move using Facade (handles all complexity)
            var pawn = board.getPiece(6, 0);
            if (pawn != null) {
                Move move = new Move(pawn, 6, 0, 5, 0);
                boolean success = gameFacade.executeMoveWithValidation(game.getId(), move);
                System.out.println("\nMove execution: " + (success ? "SUCCESS" : "FAILED"));
            }

            // 7. Get complete state (Facade returns simple data object)
            Optional<GameState> gameState = gameFacade.getCompleteGameState(game.getId());
            gameState.ifPresent(state -> {
                System.out.println("\nGame Status:");
                System.out.println("  - ID: " + state.getGameId());
                System.out.println("  - Status: " + state.getStatus());
                System.out.println("  - Current Turn: " + state.getCurrentTurn());
                System.out.println("  - Moves made: " + state.getMoveHistory().size());
            });
        }

        System.out.println("\nPATTERN SYNERGY:");
        System.out.println("✓ Decorator + DIP = Flexible piece creation with special moves");
        System.out.println("✓ Facade + Service Layer = Simple, clean API");
        System.out.println("✓ ISP + LSP = Type-safe, polymorphic piece usage");
        System.out.println("✓ All patterns = Maintainable, extensible architecture\n");
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   ChessSync Design Patterns & SOLID Principles Examples    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        decoratorPatternExample();
        dipFactoryExample();
        ispExample();
        lspExample();

        // Note: facadePatternExample and combinedPatternExample require Spring context
        // They would be called in actual application with autowired GameFacade

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          For more details, see DESIGN_PATTERNS_GUIDE.md    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
// Last modified during: feat: Implement piece movement Strategy pattern through subclasses [strategy]
