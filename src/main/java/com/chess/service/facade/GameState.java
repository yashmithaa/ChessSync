package com.chess.service.facade;

import com.chess.model.Board;
import com.chess.model.GameStatus;
import com.chess.model.Move;
import com.chess.model.Player;
import com.chess.model.pieces.PieceColor;

import java.util.List;

/**
 * Data class representing complete game state returned by GameFacade.
 * Encapsulates all necessary information for clients without exposing
 * the full complexity of the Game entity.
 */
public class GameState {
    private Long gameId;
    private Player whitePlayer;
    private Player blackPlayer;
    private Board board;
    private PieceColor currentTurn;
    private GameStatus status;
    private List<Move> moveHistory;

    public GameState(Long gameId, Player whitePlayer, Player blackPlayer, 
                     Board board, PieceColor currentTurn, GameStatus status, 
                     List<Move> moveHistory) {
        this.gameId = gameId;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.board = board;
        this.currentTurn = currentTurn;
        this.status = status;
        this.moveHistory = moveHistory;
    }

    // Getters
    public Long getGameId() {
        return gameId;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public String getWhitePlayerName() {
        return whitePlayer != null ? whitePlayer.getUsername() : "Unknown";
    }

    public String getBlackPlayerName() {
        return blackPlayer != null ? blackPlayer.getUsername() : "Unknown";
    }
}
// Last modified during: feat: Implement GameFacade unified interface for game operations [facade]
