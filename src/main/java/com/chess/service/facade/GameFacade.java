package com.chess.service.facade;

import com.chess.model.Game;
import com.chess.model.GameMode;
import com.chess.model.Move;
import com.chess.model.Player;
import com.chess.model.pieces.PieceColor;
import com.chess.service.GameService;
import com.chess.service.PlayerService;
import com.chess.util.MoveValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Facade Pattern:
 * Provides a simplified, unified interface for complex game operations.
 * Encapsulates interactions between GameService, PlayerService, and MoveValidator.
 * 
 * Benefits:
 * - Clients don't need to know about GameService, PlayerService, or MoveValidator
 * - Complex workflows are simplified into single method calls
 * - Reduces coupling between client code and service layer
 * - Makes the API easier to use and understand
 */
@Service
public class GameFacade {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    /**
     * Simplified game creation and initialization.
     * Handles player validation and game setup in one call.
     * 
     * @param whitePlayerId ID of the white player
     * @param blackPlayerId ID of the black player
     * @param gameMode The game mode (STANDARD, BLITZ, etc.)
     * @return The created game, or null if players don't exist
     */
    public Game initializeGame(Long whitePlayerId, Long blackPlayerId, GameMode gameMode) {
        Optional<Player> whitePlayer = playerService.getPlayerById(whitePlayerId);
        Optional<Player> blackPlayer = playerService.getPlayerById(blackPlayerId);

        if (whitePlayer.isPresent() && blackPlayer.isPresent()) {
            return gameService.createGame(whitePlayer.get(), blackPlayer.get(), gameMode);
        }
        return null;
    }

    /**
     * Perform a move with complete validation and state updates.
     * Handles move validation, board updates, game status changes, and player stats.
     * 
     * @param gameId The ID of the game
     * @param move The move to execute
     * @return true if move was successful, false otherwise
     */
    public boolean executeMoveWithValidation(Long gameId, Move move) {
        Optional<Game> gameOpt = gameService.getGameById(gameId);
        if (gameOpt.isEmpty()) {
            return false;
        }

        Game game = gameOpt.get();

        // Validate the move is legal
        if (!MoveValidator.isLegalMove(game.getBoard(), move)) {
            return false;
        }

        // Execute the move through the service
        return gameService.makeMove(game, move);
    }

    /**
     * Get game state with all necessary information for UI rendering.
     * Consolidates multiple service calls into one.
     * 
     * @param gameId The ID of the game
     * @return The game object with full state, or empty if not found
     */
    public Optional<GameState> getCompleteGameState(Long gameId) {
        Optional<Game> gameOpt = gameService.getGameById(gameId);
        return gameOpt.map(game -> new GameState(
                game.getId(),
                game.getWhitePlayer(),
                game.getBlackPlayer(),
                game.getBoard(),
                game.getCurrentTurn(),
                game.getStatus(),
                game.getMoveHistory()
        ));
    }

    /**
     * Resign from a game with proper state management.
     * Updates game status and player statistics automatically.
     * 
     * @param gameId The ID of the game
     * @param playerId The ID of the player resigning
     * @return true if resignation was successful, false otherwise
     */
    public boolean playerResigns(Long gameId, Long playerId) {
        return gameService.resignGame(gameId, playerId);
    }

    /**
     * Get active games for a player without exposing internal service details.
     * 
     * @param playerId The ID of the player
     * @return List of active games for the player
     */
    public List<Game> getPlayersActiveGames(Long playerId) {
        Optional<Player> player = playerService.getPlayerById(playerId);
        return player.map(p -> gameService.getActiveGamesByPlayer(p))
                    .orElse(List.of());
    }

    /**
     * Declare a draw with proper state management.
     * Updates game status and player statistics.
     * 
     * @param gameId The ID of the game to draw
     * @return true if draw was successful, false otherwise
     */
    public boolean declareDraw(Long gameId) {
        Optional<Game> gameOpt = gameService.getGameById(gameId);
        if (gameOpt.isPresent()) {
            Game game = gameOpt.get();
            game.markAsDraw();
            gameService.endGame(game, null);
            return true;
        }
        return false;
    }
}
// Last modified during: feat: Implement GameFacade unified interface for game operations [facade]
