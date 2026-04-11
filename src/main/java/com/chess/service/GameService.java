package com.chess.service;

import com.chess.model.Board;
import com.chess.model.Game;
import com.chess.model.GameMode;
import com.chess.model.Move;
import com.chess.model.Player;
import com.chess.model.pieces.PieceColor;
import com.chess.repository.GameRepository;
import com.chess.util.MoveValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;

    // Original createGame using Player objects
    public Game createGame(Player whitePlayer, Player blackPlayer, GameMode gameMode) {
        Game game = new Game(whitePlayer, blackPlayer, gameMode);
        return gameRepository.save(game);
    }

    // Overloaded createGame using player IDs
    public Game createGame(Long whitePlayerId, Long blackPlayerId, GameMode gameMode) {
        Player whitePlayer = playerService.getPlayerById(whitePlayerId).orElse(null);
        Player blackPlayer = playerService.getPlayerById(blackPlayerId).orElse(null);
        return createGame(whitePlayer, blackPlayer, gameMode);
    }

    // Get game by ID
    public Optional<Game> getGameById(Long id) {
        Optional<Game> gameOpt = gameRepository.findById(id);
        gameOpt.ifPresent(game -> {
            game.setBoard(new Board());
            game.deserializeMoves();
            // Optional: replay all moves to update board (if needed)
        });
        return gameOpt;
    }

    // Alias for getGameById
    public Optional<Game> getGame(Long id) {
        return getGameById(id);
    }

    // Get games for a specific player
    public List<Game> getGamesByPlayer(Player player) {
        return gameRepository.findAllByPlayer(player);
    }

    // Get games by player ID (for controller)
    public List<Game> getPlayerGames(Long playerId) {
        Player player = playerService.getPlayerById(playerId).orElse(null);
        return getGamesByPlayer(player);
    }

    // Get active games for a player
    public List<Game> getActiveGamesByPlayer(Player player) {
        return gameRepository.findActiveGamesByPlayer(player);
    }

    // Make move using Game object
    public boolean makeMove(Game game, Move move) {
        if (game.makeMove(move)) {
            PieceColor nextTurn = game.getCurrentTurn();

            if (MoveValidator.isCheckmate(game.getBoard(), nextTurn)) {
                PieceColor winnerColor = (nextTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
                endGame(game, winnerColor);
            } else if (MoveValidator.isStalemate(game.getBoard(), nextTurn)) {
                endGame(game, null); // Draw
            }

            game.serializeMoves();
            gameRepository.save(game);
            return true;
        }

        return false;
    }

    // Make move using game ID (for controller)
    public boolean makeMove(Long gameId, Move move) {
        Optional<Game> gameOpt = getGameById(gameId);
        if (gameOpt.isPresent()) {
            return makeMove(gameOpt.get(), move);
        }
        return false;
    }

    // End game and update player stats
    public void endGame(Game game, PieceColor winnerColor) {
        game.setEndTime(LocalDateTime.now());
        game.setWinnerColor(winnerColor);

        if (winnerColor != null) {
            boolean isWhiteWinner = (winnerColor == PieceColor.WHITE);
            playerService.updatePlayerStats(game.getWhitePlayer(), isWhiteWinner);
            playerService.updatePlayerStats(game.getBlackPlayer(), !isWhiteWinner);
        } else {
            // Draw
            playerService.updatePlayerStats(game.getWhitePlayer(), false);
            playerService.updatePlayerStats(game.getBlackPlayer(), false);
        }

        gameRepository.save(game);
    }

    // Resign from game
    public boolean resignGame(Long gameId, Long playerId) {
        Optional<Game> gameOpt = getGameById(gameId);
        if (gameOpt.isEmpty()) return false;

        Game game = gameOpt.get();
        Player player = playerService.getPlayerById(playerId).orElse(null);

        if (player == null) return false;

        PieceColor winnerColor = null;
        if (player.equals(game.getWhitePlayer())) {
            winnerColor = PieceColor.BLACK;
        } else if (player.equals(game.getBlackPlayer())) {
            winnerColor = PieceColor.WHITE;
        } else {
            return false; // Not a player in the game
        }

        endGame(game, winnerColor);
        return true;
    }
}
// Last modified during: feat: Create Game entity, GameService, and GameRepository [major]
